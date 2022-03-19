package com.alexander.orshadiarybot.service.impl;

import com.alexander.orshadiarybot.config.property.MessageProperty;
import com.alexander.orshadiarybot.config.property.UrlProperty;
import com.alexander.orshadiarybot.exception.BotException;
import com.alexander.orshadiarybot.exception.FetchDataException;
import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.parser.HtmlParser;
import com.alexander.orshadiarybot.repository.AccountRepository;
import com.alexander.orshadiarybot.service.AccountService;
import com.alexander.orshadiarybot.util.RetryUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Log4j2
public class AccountServiceImpl implements AccountService {

    private static final int RETRIES = 3;

    private RestTemplate restTemplate;
    private UrlProperty urlProperty;
    private MessageProperty messageProperty;
    private AccountRepository accountRepository;
    private HtmlParser parser;

    @Override
    @Transactional
    public Account addAccount(String phone, String password, long chatId) {
        String fullName = checkAndExtractName(phone, password);
        Account account = accountRepository.findByPhoneAndPassword(phone, password);
        if (account != null) {
            account.addOwner(chatId);
        } else {
            account = new Account();
            account.setFullName(fullName);
            account.setPhone(phone);
            account.setPassword(password);
            account.setOwners(Set.of(chatId));
        }
        return accountRepository.save(account);
    }

    @Override
    public List<Account> findByOwner(long chatId) {
        return accountRepository.findAllByOwnersContaining(chatId);
    }

    @Override
    public Account findById(long accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public String takeCookies(Account account) {
        ResponseEntity<String> response = getResponseEntity(account.getPhone(), account.getPassword());
        String cookiesSet = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (cookiesSet == null) {
            throw new BotException(String.format(messageProperty.getTakeCookiesErrorMessage(), account.getFullName()));
        }
        return cookiesSet.substring(0, cookiesSet.indexOf(';'));
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    private String checkAndExtractName(String phone, String password) {
        ResponseEntity<String> response = getResponseEntity(phone, password);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new BotException(messageProperty.getWrongCredentialsMessage());
        }
        return parser.parseFullName(response.getBody());
    }

    private ResponseEntity<String> getResponseEntity(String phone, String password) {
        MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
        postParams.add("deti_telkod", phone.substring(4, 6));
        postParams.add("deti_tel", phone.substring(6));
        postParams.add("deti_passw", password);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(postParams, httpHeaders);
        return RetryUtil.tryAndGetOrElseThrow(5 ,500L,
                () -> restTemplate.postForEntity(urlProperty.getLogin(), entity, String.class),
                () -> new FetchDataException("Cant fetch data from server after " + RETRIES + " attempts"));
    }
}
