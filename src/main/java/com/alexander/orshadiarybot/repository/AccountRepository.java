package com.alexander.orshadiarybot.repository;

import com.alexander.orshadiarybot.model.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByOwnersContaining(long owner);

    Account findByPhoneAndPassword(String phone, String password);

    Account findById(long id);
}
