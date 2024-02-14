package com.site.restauranttier.controller;

import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.UserRepository;
import com.site.restauranttier.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class MypageController {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;

    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal){
        User user = customOAuth2UserService.getUser(principal.getName());
        model.addAttribute("user",user);
        return "mypage";
    }

    @PostMapping("/api/myPage/setNickname")
    public ResponseEntity<String> setNickname(@RequestBody Map<String, String> requestBody, Principal principal){
        String newNickname=requestBody.get("newNickname");
        User user = customOAuth2UserService.getUser(principal.getName());
        user.setUserNickname(newNickname);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
