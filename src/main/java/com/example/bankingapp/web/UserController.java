package com.example.bankingapp.web;

import com.example.bankingapp.data.dto.CreateUserDto;
import com.example.bankingapp.data.dto.UpdateUserDto;
import com.example.bankingapp.data.dto.UserDto;
import com.example.bankingapp.service.UserService;
import com.example.bankingapp.service.security.AuthenticationFacade;
import com.example.bankingapp.data.model.AppUser;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Artis on 2/3/2017.
 */

@RestController
@RequestMapping(path = "user")
public class UserController {

    private final UserService userService;
    private final ModelMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public UserController(UserService userService, ModelMapper mapper, AuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.mapper = mapper;
        this.mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        this.authenticationFacade = authenticationFacade;
    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public UserDto getUser() {
        AppUser u = userService.getUser(authenticationFacade.getAuthenticatedUser().getId());
        return new UserDto(u.getName(), u.getEmail());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Validated CreateUserDto userDto) {
        AppUser appUser = userService.createUser(mapper.map(userDto, AppUser.class));
        return mapper.map(appUser, UserDto.class);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateUser(@RequestBody @Validated UpdateUserDto updateUserDto) {
        AppUser appUser = userService.getUser(authenticationFacade.getAuthenticatedUser().getId());
        mapper.map(updateUserDto, appUser);
        userService.updateUser(appUser);
    }
}
