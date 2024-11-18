package com.comesfullcircle.board.model.entity;

import jakarta.persistence.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE \"post\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE postid = ?")
//Deperecated in Hibernate 6.3
//@Where(clause = "deletedDateTime IS NULL")
@SQLRestriction("deleteddatetime IS NULL")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private ZonedDateTime createdDateTime;

    @Column
    private ZonedDateTime updatedDateTime;

    @Column
    private ZonedDateTime deletedDateTime;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ZonedDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(ZonedDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public ZonedDateTime getDeletedDateTime() {
        return deletedDateTime;
    }

    public void setDeletedDateTime(ZonedDateTime deletedDateTime) {
        this.deletedDateTime = deletedDateTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PostEntity that = (PostEntity) object;
        return Objects.equals(postId, that.postId) && Objects.equals(body, that.body) && Objects.equals(createdDateTime, that.createdDateTime) && Objects.equals(updatedDateTime, that.updatedDateTime) && Objects.equals(deletedDateTime, that.deletedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, createdDateTime, updatedDateTime, deletedDateTime);
    }
}
