package com.alexander.orshadiarybot.model.domain;

import com.alexander.orshadiarybot.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subjects")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = {})
public class Subject extends BaseEntity {

    public Subject(String name) {
        this.name = name;
    }

    @Column(unique = true)
    private String name;
}
