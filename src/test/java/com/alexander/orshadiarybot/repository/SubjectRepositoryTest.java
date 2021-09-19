package com.alexander.orshadiarybot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubjectRepositoryTest {
    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    void findAllByAccountId() {
        System.out.println(subjectRepository.findAllByAccountId(1));
    }
}