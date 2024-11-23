package com.comesfullcircle.board.repository;

import com.comesfullcircle.board.model.entity.FollowEntity;
import com.comesfullcircle.board.model.entity.LikeEntity;
import com.comesfullcircle.board.model.entity.PostEntity;
import com.comesfullcircle.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowEntityRepository extends JpaRepository<FollowEntity, Long> {
    List<FollowEntity> findByFollower(UserEntity follwer);
    List<FollowEntity> findByFollwing(UserEntity following);
    Optional<FollowEntity> findByFollowerAndFollowing(UserEntity follwer, UserEntity follwing);
}