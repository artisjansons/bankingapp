package com.example.bankingapp.data.dto;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Artis on 2/4/2017.
 */
public class UpdateUserDto extends UserDto {

    @Override
    @NotBlank
    public String getName() {
        return super.getName();
    }

    @Override
    @NotBlank
    public String getEmail() {
        return super.getEmail();
    }
}
