package com.example.bankingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Artis on 2/3/2017.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Account was not found")
public class AccountNotFoundException extends RuntimeException {

}
