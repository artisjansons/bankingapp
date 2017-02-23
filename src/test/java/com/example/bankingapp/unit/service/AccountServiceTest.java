package com.example.bankingapp.unit.service;

import com.example.bankingapp.data.repository.AccountRepository;
import com.example.bankingapp.exception.AccountBalanceExceededException;
import com.example.bankingapp.exception.AccountNotFoundException;
import com.example.bankingapp.service.AccountService;
import com.example.bankingapp.data.model.Account;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.exception.UserNotFoundException;
import com.example.bankingapp.unit.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Artis on 2/3/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService accountService;

    @Test
    public void testListAccountsForUser() throws Exception {
        AppUser user = TestUtil.getUser();
        List<Account> accounts = new ArrayList<>();
        accounts.add(TestUtil.getAccount());
        when(accountRepository.findByUser(user)).thenReturn(accounts);

        List<Account> actualAccounts = accountService.listAccountsForUser(user);
        verify(accountRepository).findByUser(user);
        assertEquals(actualAccounts.size(), accounts.size());
    }

    @Test
    public void testGetAccountForUser() throws Exception {
        AppUser user = TestUtil.getUser();
        Account account = TestUtil.getAccount();

        when(accountRepository.findByUserAndId(user, account.getId())).thenReturn(Optional.of(account));
        Account actualAccount = accountService.getAccountForUser(account.getId(), user);
        verify(accountRepository).findByUserAndId(user, account.getId());
        assertEquals(actualAccount.getId(), account.getId());
    }

    @Test(expected = AccountNotFoundException.class)
    public void testGetAccountForUserWhenAccountNotExists() throws Exception {
        AppUser user = TestUtil.getUser();
        when(accountRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());
        accountService.getAccountForUser(1L, user);
    }

    @Test
    public void testCreateAccountForUser() throws Exception {
        AppUser user = TestUtil.getUser();
        Account account = TestUtil.getAccount(user);

        when(accountRepository.save(account)).thenReturn(account);
        Account actualAccount = accountService.createAccountForUser(account);
        verify(accountRepository).save(account);
        assertEquals(account.getAccountNumber(), actualAccount.getAccountNumber());
    }

    @Test(expected = UserNotFoundException.class)
    public void testCreateAccountForNonExistingUser() throws Exception {
        Account account = TestUtil.getAccount();
        accountService.createAccountForUser(account);
    }

    @Test
    public void testWithDraw() throws Exception {
        Account account = TestUtil.getAccount();
        AppUser appUser = TestUtil.getUser();
        account.setBalance(new BigDecimal(100));
        BigDecimal amount = new BigDecimal(20);

        BigDecimal substract = account.getBalance().subtract(amount);

        when(accountRepository.findByUserAndId(appUser, account.getId())).thenReturn(Optional.of(account));

        Account actualAccount = accountService.withDraw(appUser, account.getId(), amount);
        assertEquals(actualAccount.getBalance(), substract);
    }

    @Test(expected = AccountBalanceExceededException.class)
    public void testWithDrawWithExceededBalance() throws Exception {
        Account account = TestUtil.getAccount();
        AppUser appUser = TestUtil.getUser();
        account.setBalance(new BigDecimal(10));
        BigDecimal amount = new BigDecimal(20);

        when(accountRepository.findByUserAndId(appUser, account.getId())).thenReturn(Optional.of(account));
        accountService.withDraw(appUser, account.getId(), amount);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testWithDrawWithInvalidAccount() throws Exception {
        AppUser appUser = TestUtil.getUser();
        when(accountRepository.findByUserAndId(appUser, 1L)).thenReturn(Optional.empty());
        accountService.withDraw(appUser, 1L, new BigDecimal(100));
    }

    @Test
    public void testDeposit() throws Exception {
        AppUser appUser = TestUtil.getUser();
        Account account = TestUtil.getAccount(appUser);
        BigDecimal amount = new BigDecimal(20);
        BigDecimal actualAmount = account.getBalance().add(amount);
        when(accountRepository.findByUserAndId(appUser, 1L)).thenReturn(Optional.of(account));
        accountService.deposit(appUser, 1L, amount);
        assertEquals(account.getBalance(), actualAmount);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testDepositWithInvalidAccount() throws Exception {
        AppUser appUser = TestUtil.getUser();
        when(accountRepository.findByUserAndId(appUser, 1L)).thenReturn(Optional.empty());
        accountService.deposit(appUser, 1L, new BigDecimal(20));
    }
}
