package com.alexander.orshadiarybot.model.domain;

import com.alexander.orshadiarybot.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Data
@EqualsAndHashCode(callSuper = true, of = {})
public class Account extends BaseEntity {

    private String phone;

    private String password;

    private String fullName;

    @ElementCollection
    @CollectionTable(name = "account_onwers")
    private Set<Long> owners;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMarksUpdate;

    public void addOwner(long chatId) {
        owners.add(chatId);
    }
}
