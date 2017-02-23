package com.example.bankingapp.service;

import com.example.bankingapp.data.repository.AccountRepository;
import com.example.bankingapp.exception.AccountBalanceExceededException;
import com.example.bankingapp.data.model.Account;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.exception.AccountNotFoundException;
import com.example.bankingapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by Artis on 2/3/2017.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public List<Account> listAccountsForUser(AppUser user) {
        return accountRepository.findByUser(user);
    }

    @Transactional
    public Account getAccountForUser(Long id, AppUser appUser) {
        Optional<Account> account = accountRepository.findByUserAndId(appUser, id);
        if (!account.isPresent()) {
            throw new AccountNotFoundException();
        }

        return account.get();
    }

    @Transactional
    public Account createAccountForUser(Account account) {
        if (account.getUser()==null) {
            throw new UserNotFoundException();
        }
        return accountRepository.save(account);
    }

    @Transactional
    public Account withDraw(AppUser appUser, Long accountId, BigDecimal amount) {
        Account account = getAccountForUser(accountId, appUser);
        BigDecimal balance = account.getBalance();
        if (balance.compareTo(amount)==-1) {
            throw new AccountBalanceExceededException();
        }

        account.setBalance(balance.subtract(amount));
        return account;
    }

    @Transactional
    public Account deposit(AppUser appUser, Long accountId, BigDecimal amount) {
        Account account = getAccountForUser(accountId, appUser);
        BigDecimal balance = account.getBalance();
        account.setBalance(balance.add(amount));

        return account;
    }
}
