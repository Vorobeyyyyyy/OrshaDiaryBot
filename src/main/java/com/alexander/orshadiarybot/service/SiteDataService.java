package com.alexander.orshadiarybot.service;

import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.model.domain.Mark;

import java.util.List;

public interface SiteDataService {
    List<Mark> findMarksByAccount(Account account);

    List<Mark> findMarksByAccountForLastWeeks(Account account, int weekCount);
}
