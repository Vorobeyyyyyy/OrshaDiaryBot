package com.alexander.orshadiarybot.service.impl;

import com.alexander.orshadiarybot.model.domain.Subject;
import com.alexander.orshadiarybot.repository.SubjectRepository;
import com.alexander.orshadiarybot.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private SubjectRepository subjectRepository;

    @Override
    public List<Subject> findByAccountId(long accountId) {
        return subjectRepository.findAllByAccountId(accountId);
    }

    @Override
    public Subject findById(long subjectId) {
        return subjectRepository.findById(subjectId);
    }
}
