package com.alexander.orshadiarybot.service.impl;

import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.service.AccountService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
class AccountServiceImplTest {
    @Autowired
    private AccountService accountService;

    @Test
    void takeCookies() {
        Account account = new Account();
        account.setPhone("+375295952236");
        account.setPassword("VD2236");
        System.out.println(accountService.takeCookies(account));
    }
}