package com.example.bankingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Artis on 2/3/2017.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User already exists")
public class UserAlreadyExistsException extends RuntimeException {
}
