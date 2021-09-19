package com.alexander.orshadiarybot.repository;

import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.model.domain.Mark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findAllByAccount(Account account);

    List<Mark> findAllByAccountId(long accountId);

    List<Mark> findAllByAccountIdAndSubjectId(long accountId, long subjectId);

    void deleteAllByAccount(Account account);
}