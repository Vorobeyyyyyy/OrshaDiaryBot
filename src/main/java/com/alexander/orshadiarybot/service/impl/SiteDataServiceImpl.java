package com.alexander.orshadiarybot.service.impl;

import com.alexander.orshadiarybot.config.property.UrlProperty;
import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.model.domain.Mark;
import com.alexander.orshadiarybot.parser.HtmlParser;
import com.alexander.orshadiarybot.service.AccountService;
import com.alexander.orshadiarybot.service.SiteDataService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class SiteDataServiceImpl implements SiteDataService {
    private RestTemplate restTemplate;
    private AccountService accountService;
    private UrlProperty urlProperty;
    private HtmlParser parser;

    @Override
    public List<Mark> findMarksByAccounts(Account account) {
        Calendar now = Calendar.getInstance();
        Calendar summer = Calendar.getInstance();
        summer.set(Calendar.DATE, 7);
        summer.set(Calendar.MONTH, Calendar.JULY);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DATE, 1);
        if (now.get(Calendar.MONTH) < Calendar.SEPTEMBER) {
            calendar.add(Calendar.YEAR, -1);
        } else {
            summer.add(Calendar.YEAR, 1);
        }
        String cookies = accountService.takeCookies(account);


        List<Mark> marks = new ArrayList<>();
        while (calendar.compareTo(summer) <= 0) {
            marks.addAll(requestMarksForWeek(calendar, cookies));
            calendar.add(Calendar.DATE, 7);
        }
        return marks;
    }

    private List<Mark> requestMarksForWeek(Calendar calendar, String cookies) {
        MultiValueMap<String, Integer> postParams = new LinkedMultiValueMap<>();
        postParams.add("ocenki_day", calendar.get(Calendar.DATE));
        postParams.add("ocenki_mon", calendar.get(Calendar.MONTH) + 1);
        postParams.add("ocenki_year", calendar.get(Calendar.YEAR));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Integer>> entity = new HttpEntity<>(postParams, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(urlProperty.getMarks(), entity, String.class);
        return parser.parseMarks(response.getBody());
    }
}
