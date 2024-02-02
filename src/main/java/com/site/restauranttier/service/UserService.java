package com.site.restauranttier.service;

import com.site.restauranttier.DataNotFoundException;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    /*private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입 폼에 대한 회원가입 서비스
    public User create(String userTokenId, String email, String password, String nickname) {
        User user = new User();
        if (userRepository.findByUserTokenId(userTokenId).isPresent()) {
            throw new DataIntegrityViolationException("User with id " + userTokenId + " already exists.");
        }
        user.setUserTokenId(userTokenId);
        user.setUserEmail(email);
        user.setLoginApi("self");
        user.setUserPassword(passwordEncoder.encode(password));
        user.setUserNickname(nickname);
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus("ACTIVE");
        this.userRepository.save(user);
        return user;
    }

    public User getUser(String userTokenId) {
        Optional<User> user = userRepository.findByUserTokenId(userTokenId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw  new DataNotFoundException("user not found");
        }
    }*/

}