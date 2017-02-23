package com.example.bankingapp.web;

import com.example.bankingapp.data.dto.WithdrawDto;
import com.example.bankingapp.service.UserService;
import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.service.AccountService;
import com.example.bankingapp.service.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Artis on 2/3/2017.
 */

@RestController
@RequestMapping("user/account/{id}/withdraw")
public class UserAccountWithdrawController {

    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public UserAccountWithdrawController(AuthenticationFacade authenticationFacade, UserService userService,
                                         AccountService accountService) {
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
        this.accountService = accountService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void withdraw(@RequestBody @Validated WithdrawDto withdraw, @PathVariable Long id) {
        final AppUser authenticatedUser = userService.getUser(authenticationFacade.getAuthenticatedUser().getId());
        accountService.withDraw(authenticatedUser, id, withdraw.getAmount());
    }
}
