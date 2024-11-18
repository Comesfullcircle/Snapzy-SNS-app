package com.comesfullcircle.board.model;

import com.comesfullcircle.board.model.entity.PostEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record Post(
        Long postId,
        String body,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        ZonedDateTime deletedDateTime
) {
    // from 메서드 정의
    public static Post from(PostEntity postEntity) {
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),
                postEntity.getDeletedDateTime()
        );
    }
}

