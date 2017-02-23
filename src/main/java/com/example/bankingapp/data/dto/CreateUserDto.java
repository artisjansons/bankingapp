package com.example.bankingapp.data.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Artis on 2/3/2017.
 */
public class CreateUserDto extends UserDto {

    @NotBlank
    @Length(min = 8, max = 45)
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    @Length(min = 3, max = 30)
    @NotBlank
    public String getName() {
        return super.getName();
    }

    @Override
    @Email
    @NotBlank
    public String getEmail() {
        return super.getEmail();
    }
}
