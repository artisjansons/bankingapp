package com.example.bankingapp.service.security;

import org.springframework.security.core.Authentication;

/**
 * Created by Artis on 2/3/2017.
 */
public interface IAuthenticationFacade {

    Authentication getAuthentication();

}
