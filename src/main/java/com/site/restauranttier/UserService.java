package com.site.restauranttier;

import com.site.restauranttier.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // 회원 가입 폼에 대한 회원가입 서비스
    public User create(String id, String email, String password, String nickname) {
        User user = new User();
        user.setUserId(id);
        user.setUserEmail(email);
        user.setUserPassword(passwordEncoder.encode(password));
        user.setUserNickname(nickname);
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus("ACTIVE");
        this.userRepository.save(user);
        return user;
    }
}