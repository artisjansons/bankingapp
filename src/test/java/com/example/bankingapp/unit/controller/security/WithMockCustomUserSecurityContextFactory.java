package com.example.bankingapp.unit.controller.security;

import com.example.bankingapp.data.dto.SessionUser;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.unit.TestUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashSet;

/**
 * Created by Artis on 2/3/2017.
 */
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        AppUser appUser = TestUtil.getUser();
        appUser.setPassword(annotation.password());
        appUser.setEmail(annotation.username());
        appUser.setAuthorities(new HashSet<>());

        SessionUser principal = new SessionUser(appUser);
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, annotation.password(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
