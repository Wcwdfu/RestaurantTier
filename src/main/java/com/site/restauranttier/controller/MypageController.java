package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.UserRepository;
import com.site.restauranttier.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        // 저장된 맛집 정보
        model.addAttribute("restaurantFavoriteList", user.getRestaurantFavoriteList());

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

//    @GetMapping("/myPage/myPostList")
//    public String myPostList(
//            Model model,
//            Principal principal,
//            @RequestParam(defaultValue = "전체") String postCategory,
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(defaultValue = "recent") String sort) {
//        Page<Post> paging;
//
//        return "myPostList";
//    }

    @GetMapping("/myPage/myPostList")
    public String test(Model model){
        model.addAttribute("test","테스트입니다");
        return "myPostList";
    }
}
