package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Evaluation;
import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.RestaurantFavorite;
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
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class MypageController {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @GetMapping("/myPage")
    public String myPage(
            @RequestParam(value = "menu-index", defaultValue = "0") int menuIndex,
            Model model,
            Principal principal
    ){
        User user = customOAuth2UserService.getUser(principal.getName());

        model.addAttribute("user",user);

        // 메뉴 탭 인덱스 정보
        model.addAttribute("menuIndex", menuIndex);
        // 저장된 맛집 정보
        List<RestaurantFavorite> favoriteList =  user.getRestaurantFavoriteList();
        favoriteList.sort(Comparator.comparing(RestaurantFavorite::getCreatedAt).reversed());
        model.addAttribute("restaurantFavoriteList", user.getRestaurantFavoriteList());
        // 평가한 맛집 정보
        // 나이를 기준으로 내림차순으로 정렬
        model.addAttribute("restaurantEvaluationList", favoriteList);

        return "mypage";
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PostMapping("/api/myPage/setNickname")
    public ResponseEntity<String> setNickname(@RequestBody Map<String, String> requestBody, Principal principal){
        String newNickname=requestBody.get("newNickname");
        User user = customOAuth2UserService.getUser(principal.getName());
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
