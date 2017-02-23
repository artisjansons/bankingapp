package com.example.bankingapp.unit.service;

import com.example.bankingapp.data.repository.UserRepository;
import com.example.bankingapp.exception.UserNotFoundException;
import com.example.bankingapp.service.UserService;
import com.example.bankingapp.unit.TestUtil;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.exception.UserAlreadyExistsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.example.bankingapp.unit.TestUtil.getUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Artis on 2/3/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;


    @Test
    public void testGetUserByUsername() throws Exception {

        AppUser user = TestUtil.getUser();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        AppUser actualUser = userService.getUserByUsername(user.getEmail());
        verify(userRepository).findByEmail(user.getEmail());
        assertEquals(actualUser.getEmail(), user.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetUserByNonExistingUsername() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        userService.getUserByUsername("invalid@username");
    }

    @Test
    public void testCreateUser() throws Exception {
        AppUser user = TestUtil.getUser();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        AppUser actualUser = userService.createUser(user);
        verify(userRepository).save(user);
        assertEquals(actualUser.getEmail(), user.getEmail());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testCreateUserByAlreadyExistingUsername() throws Exception {
        AppUser user = TestUtil.getUser();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        userService.createUser(user);
    }

    @Test
    public void testGetUser() throws Exception {
        AppUser user = TestUtil.getUser();
        when(userRepository.findOne(user.getId())).thenReturn(user);
        AppUser actualUser = userService.getUser(user.getId());
        assertEquals(actualUser.getEmail(), user.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetNonExistingUser() throws Exception {
        AppUser user = TestUtil.getUser();
        when(userRepository.findOne(user.getId())).thenReturn(null);
        userService.getUser(user.getId());
    }

    @Test
    public void testUpdateUser() throws Exception {
        AppUser user = TestUtil.getUser();
        when(userRepository.save(user)).thenReturn(user);
        userService.updateUser(user);
        verify(userRepository).save(user);
    }
}
