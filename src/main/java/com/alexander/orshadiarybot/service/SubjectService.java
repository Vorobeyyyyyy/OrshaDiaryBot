package com.alexander.orshadiarybot.service;

import com.alexander.orshadiarybot.model.domain.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findByAccountId(long accountId);

    Subject findById(long subjectId);
}
