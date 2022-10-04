package com.alexander.orshadiarybot.service.impl;

import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.service.SiteDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SiteDataServiceImplTest {
    @Autowired
    private SiteDataService siteDataService;

    @Test
    void findMarksByAccounts() {
        Account account = new Account();
        account.setPhone("+375295952236");
        account.setPassword("VD2236");
        System.out.println(siteDataService.findMarksByAccount(account));
    }
}