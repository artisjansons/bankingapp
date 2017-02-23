package com.example.bankingapp.unit.service;

import com.example.bankingapp.data.dto.SessionUser;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.exception.UserNotFoundException;
import com.example.bankingapp.service.CustomUserDetailsService;
import com.example.bankingapp.service.UserService;
import com.example.bankingapp.unit.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Artis on 2/5/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomUserDetailServiceTest {

    @Mock
    UserService userService;

    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    @Test
    public void testLoadUserByUsername() throws Exception {
        AppUser appUser = TestUtil.getUser();
        when(userService.getUserByUsername("user@example.com")).thenReturn(appUser);

        SessionUser sessionUser = (SessionUser) customUserDetailsService.loadUserByUsername("user@example.com");
        verify(userService).getUserByUsername("user@example.com");
        assertThat(sessionUser.getUser().getId(), is(appUser.getId()));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameWithInvalidUsername() throws Exception {
        when(userService.getUserByUsername("user@example.com")).thenThrow(new UserNotFoundException());
        customUserDetailsService.loadUserByUsername("user@example.com");
    }
}
