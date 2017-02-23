package com.example.bankingapp.data.repository;

import com.example.bankingapp.data.model.Account;
import com.example.bankingapp.data.model.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Artis on 2/3/2017.
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

    List<Account> findByUser(AppUser user);
    Optional<Account> findById(Long id);
    Optional<Account> findByUserAndId(AppUser user, Long id);
}
