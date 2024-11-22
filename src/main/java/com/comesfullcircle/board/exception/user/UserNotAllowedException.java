package com.comesfullcircle.board.exception.user;

import com.comesfullcircle.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotAllowedException extends ClientErrorException {

    public UserNotAllowedException() {super(HttpStatus.FORBIDDEN, "User not allowed");}
}
