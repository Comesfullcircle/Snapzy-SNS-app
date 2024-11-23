package com.comesfullcircle.board.service;

import com.comesfullcircle.board.exception.follow.FollowAlreadyExistException;
import com.comesfullcircle.board.exception.follow.InvalidFollowException;
import com.comesfullcircle.board.exception.user.UserAlreadyExistException;
import com.comesfullcircle.board.exception.user.UserNotAllowedException;
import com.comesfullcircle.board.exception.user.UserNotFoundException;
import com.comesfullcircle.board.model.entity.FollowEntity;
import com.comesfullcircle.board.model.entity.UserEntity;
import com.comesfullcircle.board.model.user.User;
import com.comesfullcircle.board.model.user.UserAuthenticationResponse;
import com.comesfullcircle.board.model.user.UserPatchRequestBody;
import com.comesfullcircle.board.repository.FollowEntityRepository;
import com.comesfullcircle.board.repository.UserEntityRepository;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.SQLDelete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private FollowEntityRepository followEntityRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public User signUp(String username, String password) {
        if (userEntityRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistException();
        }

        var userEntity
                = userEntityRepository.save(UserEntity.of(username, passwordEncoder.encode(password)));

        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(String username, String password) {
        var userEntity =
                userEntityRepository.findByUsername(username)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found: " + username));

        if(passwordEncoder.matches(password, userEntity.getPassword())) {
            var accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        }else{
            throw new UserNotFoundException();
        }
    }

    public List<User> getUsers(String query){
        List<UserEntity> userEntities;

        if(query != null && !query.isBlank()){
            // query 검색어 기반, 해당 검색어가 username에 포함되어 있는 유저목록 가져오기
            userEntities = userEntityRepository.findByUsernameContaining(query);
        }else{
            userEntities = userEntityRepository.findAll();
        }

        return userEntities.stream().map(User::from).toList();
    }

    public User getUser(String username) {
        var userEntity =
                userEntityRepository.findByUsername(username)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found: " + username));

        return User.from(userEntity);
    }

    public User updateUser(
            String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {
        var userEntity =
                userEntityRepository
                        .findByUsername(username)
                        .orElseThrow(()-> new UsernameNotFoundException(username));

        if (!userEntity.equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        if (userPatchRequestBody.description() != null) {
            userEntity.setDescrption(userPatchRequestBody.description());
        }

        return User.from(userEntityRepository.save(userEntity));

    }

    @Transactional
    public User follow(String username, UserEntity currentUser) {
        var following =
                userEntityRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found: " + username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A user cannot follow themselves.");
        }
        followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .ifPresent(
                        followEntity -> {
                            throw new FollowAlreadyExistException(currentUser, following);
                        }
                );
        followEntityRepository.save(
                FollowEntity.of(currentUser, following)
        );

        following.setFollwersCount(following.getFollwersCount() + 1);
        currentUser.setFollwingsCount(following.getFollwingsCount()+1);

        userEntityRepository.save(currentUser);
        userEntityRepository.save(following);
        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following);
    }

    @Transactional
    public User unfollow(String username, UserEntity currentUser) {
        var following =
                userEntityRepository.findByUsername(username)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found: " + username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A user cannot unfollow themselves.");
        }
        var followEntity =  followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .orElseThrow(()-> new FollowAlreadyExistException(currentUser, following));
        followEntityRepository.delete(
                FollowEntity.of(currentUser, following)
        );

        following.setFollwersCount(Math.max(0, following.getFollwersCount() - 1));
        currentUser.setFollwingsCount(Math.max(0, following.getFollwingsCount() - 1));

        userEntityRepository.save(currentUser);
        userEntityRepository.save(following);
        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following);
    }
}
