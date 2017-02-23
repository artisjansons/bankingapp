package com.example.bankingapp.util;

import java.util.UUID;

/**
 * Created by Artis on 2/3/2017.
 */
public class AccountUtil {

    public static String createAccountNumber() {
        return UUID.randomUUID().toString();
    }

}
