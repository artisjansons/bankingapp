package com.example.bankingapp.data.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by Artis on 2/3/2017.
 */
@Entity
public class AppUser implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private Set<AppUserAuthority> authorities;

    @OneToMany(mappedBy = "user")
    private Set<Account> accounts;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Set<AppUserAuthority> authorities) {
        this.authorities = authorities;
    }

    public Set<AppUserAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
