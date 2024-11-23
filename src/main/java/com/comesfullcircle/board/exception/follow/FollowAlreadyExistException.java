package com.comesfullcircle.board.exception.follow;

import com.comesfullcircle.board.exception.ClientErrorException;
import com.comesfullcircle.board.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class FollowAlreadyExistException extends ClientErrorException {

    public FollowAlreadyExistException() {
        super(HttpStatus.CONFLICT, "Follow already exists.");
    }

    public FollowAlreadyExistException(UserEntity follower, UserEntity following) {
        super(HttpStatus.CONFLICT, "Follow with follower " + follower.getUsername()
                +" and following " + following.getUsername()
                + " already exists.");
    }
}
