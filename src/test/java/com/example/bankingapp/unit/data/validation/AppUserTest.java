package com.example.bankingapp.unit.data.validation;

import com.example.bankingapp.data.model.AppUser;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by Artis on 2/5/2017.
 */
public class AppUserTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void nameIsNull() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setName(null);
        appUser.setEmail("user@example.com");
        appUser.setPassword("strongpassword");

        Set<ConstraintViolation<AppUser>> constraintViolations =
                validator.validate( appUser );

        assertEquals( 1, constraintViolations.size());
        assertEquals("may not be empty", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void passwordIsNull() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setName("User");
        appUser.setEmail("user@example.com");
        appUser.setPassword(null);

        Set<ConstraintViolation<AppUser>> constraintViolations =
                validator.validate( appUser );

        assertEquals( 1, constraintViolations.size());
        assertEquals("may not be empty", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void emailIsNull() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setName("User");
        appUser.setEmail(null);
        appUser.setPassword("strongpassword");

        Set<ConstraintViolation<AppUser>> constraintViolations =
                validator.validate( appUser );

        assertEquals( 1, constraintViolations.size());
        assertEquals("may not be empty", constraintViolations.iterator().next().getMessage());
    }
}
