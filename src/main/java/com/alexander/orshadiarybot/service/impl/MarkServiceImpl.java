package com.alexander.orshadiarybot.service.impl;

import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.model.domain.Mark;
import com.alexander.orshadiarybot.model.domain.Subject;
import com.alexander.orshadiarybot.repository.MarkRepository;
import com.alexander.orshadiarybot.repository.SubjectRepository;
import com.alexander.orshadiarybot.service.MarkService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class MarkServiceImpl implements MarkService {
    private MarkRepository markRepository;
    private SubjectRepository subjectRepository;

    @Override
    public List<Mark> findMarksByAccount(Account account) {
        return markRepository.findAllByAccount(account);
    }

    @Override
    public List<Mark> findMarksByAccount(long accountId) {
        return markRepository.findAllByAccountId(accountId);
    }

    @Override
    public List<Mark> findMarksByAccountAndSubjectId(long accountId, long subjectId) {
        return markRepository.findAllByAccountIdAndSubjectId(accountId, subjectId);
    }

    @Override
    @Transactional
    public void rebaseMarks(Account account, List<Mark> marks) {
        List<Subject> subjects = subjectRepository.findAll();
        markRepository.deleteAllByAccount(account);
        marks.forEach(mark -> mark.setAccount(account));
        marks.forEach(mark -> subjects
                .stream()
                .filter(s -> s.getName().equals(mark.getSubject().getName()))
                .findAny().ifPresentOrElse(
                        mark::setSubject,
                        () -> mark.setSubject(subjectRepository.save(mark.getSubject()))));
        markRepository.saveAll(marks);
    }
}
