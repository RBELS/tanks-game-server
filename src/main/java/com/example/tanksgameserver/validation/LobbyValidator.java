package com.example.tanksgameserver.validation;

import com.example.tanksgameserver.responsemodel.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class LobbyValidator {
    private static final int MAX_USERNAME_LENGTH = 20;
    public ErrorResponse validateUsername(String username) {
        if (username.length() > MAX_USERNAME_LENGTH)
            return new ErrorResponse("Username is too long.", HttpStatus.BAD_REQUEST);
        //check if username exists
        return null;
    }
}
