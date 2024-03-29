package com.alexander.orshadiarybot.service.impl;

import com.alexander.orshadiarybot.config.property.UrlProperty;
import com.alexander.orshadiarybot.exception.FetchDataException;
import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.model.domain.Mark;
import com.alexander.orshadiarybot.parser.HtmlParser;
import com.alexander.orshadiarybot.service.AccountService;
import com.alexander.orshadiarybot.service.SiteDataService;
import com.alexander.orshadiarybot.util.CalendarUtils;
import com.alexander.orshadiarybot.util.RetryUtil;
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

    private static final int RETRIES = 3;

    private RestTemplate restTemplate;
    private AccountService accountService;
    private UrlProperty urlProperty;
    private HtmlParser parser;

    @Override
    public List<Mark> findMarksByAccount(Account account) {
        Calendar now = CalendarUtils.createCalendar();
        Calendar summer = CalendarUtils.createCalendar();
        summer.set(Calendar.DATE, 7);
        summer.set(Calendar.MONTH, Calendar.JULY);

        Calendar calendar = CalendarUtils.createCalendar();
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

    @Override
    public List<Mark> findMarksByAccountForLastWeeks(Account account, int weekCount) {
        Calendar calendar = CalendarUtils.createCalendar();

        String cookies = accountService.takeCookies(account);

        List<Mark> marks = new ArrayList<>();
        for (int i = 0; i < weekCount; i++) {
            marks.addAll(requestMarksForWeek(calendar, cookies));
            calendar.add(Calendar.DATE,-7);
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
        ResponseEntity<String> response = RetryUtil.tryAndGetOrElseThrow(RETRIES, 500L,
                () -> restTemplate.postForEntity(urlProperty.getMarks(), entity, String.class),
                () -> new FetchDataException("Can't get marks for " + calendar.getTime()));
        return parser.parseMarks(response.getBody());
    }
}
