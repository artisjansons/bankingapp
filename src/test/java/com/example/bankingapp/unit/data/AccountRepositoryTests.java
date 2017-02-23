package com.example.bankingapp.unit.data;

import com.example.bankingapp.data.model.Account;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.data.repository.AccountRepository;
import com.example.bankingapp.data.repository.UserRepository;
import com.example.bankingapp.util.AccountUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by Artis on 2/4/2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testFindById() throws Exception {
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(AccountUtil.createAccountNumber());
        
        testEntityManager.persist(account);
        Account actualAccount = accountRepository.findById(account.getId()).get();
        assertThat(actualAccount.getAccountNumber()).isEqualTo(account.getAccountNumber());
    }

    @Test
    public void testFindByUser() throws Exception {
        AppUser user = new AppUser();
        user.setName("User");
        user.setEmail("user@example.com");
        user.setPassword("strongpassword");

        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(AccountUtil.createAccountNumber());
        account.setBalance(BigDecimal.ZERO);

        testEntityManager.persist(user);
        testEntityManager.persist(account);

        List<Account> accounts = accountRepository.findByUser(user);
        assertThat(accounts.size()).isEqualTo(1);
        assertThat(accounts.get(0).getAccountNumber()).isEqualTo(account.getAccountNumber());
    }

    @Test
    public void testFindByUserAndId() throws Exception {
        AppUser user = new AppUser();
        user.setEmail("user@user");
        user.setPassword("qwerty");
        user.setName("User");

        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(AccountUtil.createAccountNumber());
        account.setUser(user);

        testEntityManager.persist(user);
        testEntityManager.persist(account);

        Account actualAccount = accountRepository.findByUserAndId(user, account.getId()).get();
        assertThat(actualAccount.getAccountNumber()).isEqualTo(account.getAccountNumber());
    }
}
