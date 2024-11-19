package com.comesfullcircle.board.exception.user;

import com.comesfullcircle.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends ClientErrorException {

    public UserAlreadyExistException() {
        super(HttpStatus.CONFLICT, "User already exists.");
    }

    public UserAlreadyExistException(String username) {
        super(HttpStatus.CONFLICT, "User with username " + username + " already exists.");
    }
}
