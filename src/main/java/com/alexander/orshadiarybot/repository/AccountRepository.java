package com.alexander.orshadiarybot.repository;

import com.alexander.orshadiarybot.model.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByOwnersContaining(long owner);

    Account findByPhoneAndPassword(String phone, String password);

    Account findById(long id);

    Optional<Account> findTopByLastMarksUpdateBeforeOrderByLastMarksUpdateAsc(Date maxDate);
}
