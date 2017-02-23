package com.example.bankingapp.web;

import com.example.bankingapp.data.model.Account;
import com.example.bankingapp.service.AccountService;
import com.example.bankingapp.service.UserService;
import com.example.bankingapp.service.security.AuthenticationFacade;
import com.example.bankingapp.data.dto.AccountDto;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.util.AccountUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Artis on 2/3/2017.
 */

@RestController
@RequestMapping("user/account")
public class UserAccountController {

    private final AuthenticationFacade authenticationFacade;

    private final UserService userService;

    private final AccountService accountService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserAccountController(AuthenticationFacade authenticationFacade, UserService userService, AccountService accountService, ModelMapper modelMapper) {
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AccountDto> listAccounts() {
        AppUser appUser = userService.getUser(authenticationFacade.getAuthenticatedUser().getId());
        return accountService.listAccountsForUser(appUser).stream()
                .map(this::mapAccountToAccountDto).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public AccountDto getAccount(@PathVariable Long id) {
        return modelMapper.map(accountService.getAccountForUser(
                id,
                userService.getUser(authenticationFacade.getAuthenticatedUser().getId())
        ), AccountDto.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount() {
        Account account = new Account();
        account.setUser(userService.getUser(authenticationFacade.getAuthenticatedUser().getId()));
        account.setAccountNumber(AccountUtil.createAccountNumber());
        Account savedAccount = accountService.createAccountForUser(account);

        return mapAccountToAccountDto(savedAccount);
    }

    private AccountDto mapAccountToAccountDto(Account account) {
        AccountDto dto = modelMapper.map(account, AccountDto.class);
        dto.add(linkTo(methodOn(UserAccountController.class).getAccount(account.getId())).withSelfRel());

        return dto;
    }

}
