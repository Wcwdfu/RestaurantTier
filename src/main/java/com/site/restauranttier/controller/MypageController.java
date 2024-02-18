package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.UserRepository;
import com.site.restauranttier.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class MypageController {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal){
        User user = customOAuth2UserService.getUser(principal.getName());

        model.addAttribute("user",user);

        // 저장된 맛집 정보
        model.addAttribute("restaurantFavoriteList", user.getRestaurantFavoriteList());
        // 평가한 맛집 정보
        model.addAttribute("restaurantEvaluationList", user.getEvaluationList());

        // 커뮤나티관련 정보
        model.addAttribute("postList",user.getPostList());
        model.addAttribute("postCommentList",user.getPostCommentList());
        model.addAttribute("postScrabList",user.getScrapList());


        return "mypage";
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PostMapping("/api/myPage/setNickname")
    public ResponseEntity<String> setNickname(@RequestBody Map<String, String> requestBody, Principal principal){
        String newNickname=requestBody.get("newNickname");
        User user = customOAuth2UserService.getUser(principal.getName());

        //전과 동일한 닉네임
        if(newNickname.equals(user.getUserNickname())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이전과 동일한 닉네임입니다.");
        }
        // 닉네임이 2글자 이하인 경우
        if (newNickname.length() < 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임은 2자 이상이어야 합니다.");
        }
        // 닉네임이 10자 이상인 경우
        if (newNickname.length() > 15) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임은 15자 이하여야 합니다.");
        }
        // 닉네임이 이미 존재하는 경우
        Optional<User> userOptional =  userRepository.findByUserNickname(newNickname);
        if (userOptional.isPresent() && !newNickname.equals(user.getUserNickname())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("해당 닉네임이 이미 존재합니다.");
        }

        user.setUserNickname(newNickname);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

}
