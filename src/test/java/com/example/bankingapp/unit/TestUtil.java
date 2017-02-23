package com.example.bankingapp.unit;

import com.example.bankingapp.data.model.Account;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.util.AccountUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Artis on 2/3/2017.
 */
public class TestUtil {

    public static AppUser getUser() {
        AppUser user = new AppUser();
        user.setEmail("user@user.com");
        user.setId(1L);
        user.setName("User");
        user.setPassword("strongpassword");
        return user;
    }

    public static AppUser getUser(String username, Long id, String name) {
        AppUser user = new AppUser();
        user.setEmail(username);
        user.setId(id);
        user.setName(name);
        return user;
    }

    public static Account getAccount(AppUser appUser) {
        Account account = getAccount();
        account.setUser(appUser);

        return account;
    }

    public static Account getAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber(AccountUtil.createAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedDate(new Date());
        return account;
    }

}
