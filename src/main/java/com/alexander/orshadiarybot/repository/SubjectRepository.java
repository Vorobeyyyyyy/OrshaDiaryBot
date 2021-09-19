package com.alexander.orshadiarybot.repository;

import com.alexander.orshadiarybot.model.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query(value = "SELECT s from Subject s join Mark m on m.subject.id=s.id WHERE m.account.id= ?1")
    List<Subject> findAllByAccountId(long accountId);

    Subject findById(long subjectId);
}
