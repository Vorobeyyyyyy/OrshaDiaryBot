package com.alexander.orshadiarybot.service;

import com.alexander.orshadiarybot.model.domain.Account;

import java.util.List;

public interface AccountService {
    Account addAccount(String phone, String password, long chatId);

    List<Account> findByOwner(long chatId);

    Account findById(long accountId);

    String takeCookies(Account account);

    List<Account> findAll();
}
