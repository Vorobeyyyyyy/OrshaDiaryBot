package com.alexander.orshadiarybot.model.domain;

import com.alexander.orshadiarybot.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
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

    public void addOwner(long chatId) {
        owners.add(chatId);
    }
}
