package com.alexander.orshadiarybot.service.impl;

import com.alexander.orshadiarybot.model.domain.Subject;
import com.alexander.orshadiarybot.repository.SubjectRepository;
import com.alexander.orshadiarybot.service.MarkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MarkServiceImplTest {
    @Autowired
    private MarkService markService;
    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    void rebaseMarks() {
//        Account account = new Account();
//        account.setPhone("+223");
//        account.setPassword("sf1");
//        Mark mark1 = new Mark();
//        mark1.setSubject(new Subject("Math"));
//        Mark mark2 = new Mark();
//        mark2.setSubject(new Subject("Math"));
//
//        markService.rebaseMarks(account, List.of(mark1, mark2));
    }
}