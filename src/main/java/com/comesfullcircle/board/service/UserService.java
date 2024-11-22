package com.comesfullcircle.board.service;

import com.comesfullcircle.board.exception.user.UserAlreadyExistException;
import com.comesfullcircle.board.exception.user.UserNotAllowedException;
import com.comesfullcircle.board.exception.user.UserNotFoundException;
import com.comesfullcircle.board.model.entity.UserEntity;
import com.comesfullcircle.board.model.user.User;
import com.comesfullcircle.board.model.user.UserAuthenticationResponse;
import com.comesfullcircle.board.model.user.UserPatchRequestBody;
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

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserEntityRepository userEntityRepository;
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
}
