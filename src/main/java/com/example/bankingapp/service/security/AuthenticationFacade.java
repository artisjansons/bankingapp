package com.example.bankingapp.service.security;

import com.example.bankingapp.data.dto.SessionUser;
import com.example.bankingapp.data.model.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by Artis on 2/3/2017.
 */

@Service
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public AppUser getAuthenticatedUser() {
        return ((SessionUser) getAuthentication().getPrincipal()).getUser();
    }
}
