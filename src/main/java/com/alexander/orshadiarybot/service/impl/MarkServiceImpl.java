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
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
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
        markRepository.deleteAllByAccount(account);

        setAccountAndSubjectToMarks(account, marks);
        markRepository.saveAll(marks);
        account.setLastMarksUpdate(new Date());
    }


    @Override
    @Transactional
    public void rebaseLastWeeksMarks(Account account, int weekCount, List<Mark> marks) {
        LocalDate date = getWeekStartDate(weekCount);

        markRepository.deleteAllByAccountIdAndDateAfterOrEqual(account.getId(), date);

        setAccountAndSubjectToMarks(account, marks);
        markRepository.saveAll(marks);
        account.setLastMarksSoftUpdate(new Date());
    }

    @Override
    public List<Mark> findMarksByAccountForLastWeeks(long accountId, int softUpdateWeekCount) {
        LocalDate date = getWeekStartDate(softUpdateWeekCount);

        return markRepository.findAllByAccountIdAndDateFrom(accountId, date);
    }

    private LocalDate getWeekStartDate(int weekCount) {
        if (weekCount < 1) {
            throw new IllegalArgumentException("Min weekCount is 1");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, (weekCount - 1) * -7);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        LocalDate date = LocalDate.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
        return date;
    }

    private void setAccountAndSubjectToMarks(Account account, List<Mark> marks) {
        marks.forEach(mark -> mark.setAccount(account));
        List<Subject> subjects = subjectRepository.findAll();
        marks.forEach(mark -> subjects
                .stream()
                .filter(s -> s.getName().equals(mark.getSubject().getName()))
                .findAny()
                .ifPresentOrElse(
                        mark::setSubject,
                        () -> persistAndSet(subjects, mark)
                )
        );
    }

    private void persistAndSet(List<Subject> subjects, Mark mark) {
        Subject persistedSubject = subjectRepository.save(mark.getSubject());
        subjects.add(persistedSubject);
        mark.setSubject(persistedSubject);
    }
}
