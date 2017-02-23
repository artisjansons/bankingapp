package com.example.bankingapp.service;

import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.data.model.AppUserAuthority;
import com.example.bankingapp.data.repository.UserRepository;
import com.example.bankingapp.exception.UserAlreadyExistsException;
import com.example.bankingapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Artis on 2/3/2017.
 */
@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AppUser getUserByUsername(String email) {
        Optional<AppUser> appUser = repository.findByEmail(email);
        if (!appUser.isPresent()) {
            throw new UserNotFoundException();
        }
        return appUser.get();
    }

    @Transactional
    public AppUser getUser(Long id) {
        Optional<AppUser> appUser = Optional.ofNullable(repository.findOne(id));
        if (!appUser.isPresent()) {
            throw new UserNotFoundException();
        }

        return appUser.get();
    }

    @Transactional
    public AppUser createUser(AppUser appUser) {
        final Optional<AppUser> u = repository.findByEmail(appUser.getEmail());
        if (u.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        appUser.setPassword(new BCryptPasswordEncoder().encode(appUser.getPassword()));
        appUser.setAuthorities(initUserAuthorities());

        return repository.save(appUser);
    }

    @Transactional
    public AppUser updateUser(AppUser appUser) {
        return repository.save(appUser);
    }

    private Set<AppUserAuthority> initUserAuthorities() {
        final Set<AppUserAuthority> authoritySet = new HashSet<>();
        AppUserAuthority appUserAuthority = new AppUserAuthority();
        appUserAuthority.setAuthority("ROLE_USER");
        authoritySet.add(appUserAuthority);
        return authoritySet;
    }
}
