package com.example.bankingapp.data.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Artis on 2/3/2017.
 */

@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String accountNumber;

    @NotNull
    private BigDecimal balance;

    @NotNull
    private Date createdDate;

    @ManyToOne
    private AppUser user;

    @PrePersist
    public void onCreate() {
        createdDate = new Date();
        if (balance==null) {
            balance = BigDecimal.ZERO;
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
