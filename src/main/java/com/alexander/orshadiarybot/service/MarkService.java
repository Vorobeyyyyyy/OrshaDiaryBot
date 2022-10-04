package com.alexander.orshadiarybot.service;

import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.model.domain.Mark;

import java.util.List;

public interface MarkService {
    List<Mark> findMarksByAccount(Account account);

    List<Mark> findMarksByAccount(long accountId);

    List<Mark> findMarksByAccountAndSubjectId(long accountId, long subjectId);

    void rebaseMarks(Account account, List<Mark> marks);

    void rebaseLastWeeksMarks(Account account, int weekCount, List<Mark> marks);

    List<Mark> findMarksByAccountForLastWeeks(long accountId, int softUpdateWeekCount);
}
