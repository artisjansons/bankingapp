package com.example.bankingapp.data.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Artis on 2/3/2017.
 */

@Entity
public class AppUserAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue
    private Long id;

    private String authority;

    @ManyToOne
    AppUser user;

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
