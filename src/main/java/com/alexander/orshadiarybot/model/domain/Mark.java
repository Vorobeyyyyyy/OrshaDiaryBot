package com.alexander.orshadiarybot.model.domain;

import com.alexander.orshadiarybot.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "marks")
@Data
@EqualsAndHashCode(callSuper = true, of = {})
public class Mark extends BaseEntity {
    @ManyToOne
    private Account account;

    @ManyToOne
    private Subject subject;

    private int value;

    private LocalDate date;
}
