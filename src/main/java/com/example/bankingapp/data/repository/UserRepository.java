package com.example.bankingapp.data.repository;


import com.example.bankingapp.data.model.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by Artis on 2/3/2017.
 */
public interface UserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

}
