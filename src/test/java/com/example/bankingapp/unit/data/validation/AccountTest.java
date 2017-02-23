package com.example.bankingapp.unit.data.validation;

import com.example.bankingapp.data.model.Account;
import com.example.bankingapp.unit.TestUtil;
import com.example.bankingapp.util.AccountUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by Artis on 2/5/2017.
 */
public class AccountTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testAccountNumberIsNull() throws Exception {
        Account account = new Account();
        account.setAccountNumber(null);
        account.setBalance(BigDecimal.ZERO);
        account.setUser(TestUtil.getUser());
        account.setId(1L);
        account.setCreatedDate(new Date());

        Set<ConstraintViolation<Account>> constraintViolations =
                validator.validate(account);

        assertEquals( 1, constraintViolations.size());
        assertEquals("may not be empty", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testCreatedDateIsNull() throws Exception {
        Account account = new Account();
        account.setAccountNumber(AccountUtil.createAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setUser(TestUtil.getUser());
        account.setId(1L);
        account.setCreatedDate(null);

        Set<ConstraintViolation<Account>> constraintViolations =
                validator.validate(account);

        assertEquals( 1, constraintViolations.size());
        assertEquals("may not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testBalanceIsNull() throws Exception {
        Account account = new Account();
        account.setAccountNumber(AccountUtil.createAccountNumber());
        account.setBalance(null);
        account.setUser(TestUtil.getUser());
        account.setId(1L);
        account.setCreatedDate(new Date());

        Set<ConstraintViolation<Account>> constraintViolations =
                validator.validate(account);

        assertEquals( 1, constraintViolations.size());
        assertEquals("may not be null", constraintViolations.iterator().next().getMessage());
    }
}
