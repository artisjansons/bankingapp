package com.example.bankingapp.data.dto;

import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Artis on 2/3/2017.
 */
public class AccountDto extends ResourceSupport {

    private String accountNumber;
    private BigDecimal balance;
    private Date created;

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
