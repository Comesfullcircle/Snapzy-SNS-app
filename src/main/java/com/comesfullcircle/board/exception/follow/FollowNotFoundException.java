package com.comesfullcircle.board.exception.follow;

import com.comesfullcircle.board.exception.ClientErrorException;
import com.comesfullcircle.board.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class FollowNotFoundException extends ClientErrorException {

    public FollowNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Follow not found");
    }

    public FollowNotFoundException(UserEntity follower, UserEntity following) {
        super(HttpStatus.NOT_FOUND, "Follow with follower " + follower.getUsername()
                +" and following " + following.getUsername()
                + " not found.");
    }
}
