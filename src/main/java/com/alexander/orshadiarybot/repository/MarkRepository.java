package com.alexander.orshadiarybot.repository;

import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.model.domain.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findAllByAccount(Account account);

    List<Mark> findAllByAccountId(long accountId);

    List<Mark> findAllByAccountIdAndSubjectId(long accountId, long subjectId);

    void deleteAllByAccount(Account account);

    @Modifying
    @Query("delete from Mark m where m.date >= :from and m.account.id = :accountId")
    void deleteAllByAccountIdAndDateAfterOrEqual(long accountId, LocalDate from);

    @Query("select m from Mark m where m.date >= :from and m.account.id = :accountId")
    List<Mark> findAllByAccountIdAndDateFrom(long accountId, LocalDate from);
}