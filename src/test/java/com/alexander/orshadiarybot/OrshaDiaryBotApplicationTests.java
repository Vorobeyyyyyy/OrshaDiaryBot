package com.alexander.orshadiarybot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

class OrshaDiaryBotApplicationTests {

    @Test
    void contextLoads() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Integer> postParams = new LinkedMultiValueMap<>();
        postParams.add("ocenki_day", 5);
        postParams.add("ocenki_mon", 1);
        postParams.add("ocenki_year", 2022);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add(HttpHeaders.COOKIE, "617798e3ca847494e3e028670e11c0f9=bt6g2jl060jb376ckdk532cbg1");
        HttpEntity<MultiValueMap<String, Integer>> entity = new HttpEntity<>(postParams, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity("http://diary.goroo-orsha.by/index.php?option=com_content&view=article&id=10&Itemid=13", entity, String.class);
        System.out.println(response.getBody());
    }

}
