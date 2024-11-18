package com.comesfullcircle.board.model.user;

import com.comesfullcircle.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record User(
        Long userId,
        String username,
        String profile,
        String description,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime
) {

    public static User from(UserEntity userEntity) {
        return new User(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getProfile(),
                userEntity.getDescrption(),
                userEntity.getCreatedDateTime(),
                userEntity.getUpdatedDateTime()
        );
    }
}
