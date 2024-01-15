package com.site.restauranttier;

import com.site.restauranttier.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(String userNickname, String email, String password) {
        User user = new User();
        user.setUserNickname(userNickname);
        user.setUserEmail(email);
        user.setUserPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }
}