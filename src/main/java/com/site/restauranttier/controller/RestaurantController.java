package com.site.restauranttier.controller;

import com.site.restauranttier.DataNotFoundException;
import com.site.restauranttier.entity.*;
import com.site.restauranttier.etc.SortComment;
import com.site.restauranttier.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class RestaurantController {
    private final RestaurantCommentService restaurantCommentService;
    private final EvaluationService evaluatioanService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final RestaurantService restaurantService;
    private final RestaurantFavoriteService restaurantFavoriteService;

    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);
    
    @Value("${restaurant.initialDisplayMenuCount}")
    private int initialDisplayMenuCount;

    @GetMapping("/restaurants/{restaurantId}")
    public String restaurant(
            Model model,
            @PathVariable Integer restaurantId,
            Principal principal
    ) {
        // 식당 정보
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        model.addAttribute("restaurant", restaurant);
        // 메뉴
        List<RestaurantMenu> restaurantMenus = restaurantService.getRestaurantMenuList(restaurantId);
        model.addAttribute("menus", restaurantMenus);
        // 메뉴 펼치기 이전에 몇개의 메뉴를 보여줄 것인가
        model.addAttribute("initialDisplayMenuCount", initialDisplayMenuCount);
        // 식당 댓글
        List<Object[]> restaurantComments = restaurantCommentService.getCommentList(restaurantId, SortComment.POPULAR);
        model.addAttribute("restaurantComments", restaurantComments);

        // 평가하기 버튼
        // 로그인 안되어있을 경우
        if (principal == null) {
            model.addAttribute("evaluationButton", " 평가하기");
            return "restaurant";
        } else {
            // 유저
            model.addAttribute("user", customOAuth2UserService.getUser(principal.getName()));
            // 즐겨찾기 여부
            model.addAttribute("isFavoriteExist", restaurantFavoriteService.isFavoriteExist(principal.getName(), restaurantId));
            String name = principal.getName();
            User user = customOAuth2UserService.getUser(name);
            Evaluation evaluation = evaluatioanService.getByUserAndRestaurant(user, restaurant);
            if (evaluation!=null) {
                model.addAttribute("evaluationButton", "다시 평가하기");
            } else {
                model.addAttribute("evaluationButton", " 평가하기");
            }
            return "restaurant";
        }

    }

    // 해당 cuisine에 맞는 식당 목록 반환
    @GetMapping("/api/restaurants")
    public ResponseEntity<List<Restaurant>> getRestaurantsByCuisine(
            @RequestParam(value = "cuisine", defaultValue = "전체") String cuisine
    ) {
        List<Restaurant> restaurants = restaurantService.getRestaurantList(cuisine);

        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }


    // 식당 메뉴 반환
    @GetMapping("/api/restaurants/{restaurantId}/menus")
    public ResponseEntity<List<RestaurantMenu>> getRestaurantMenusByRestaurantId(
            @PathVariable Integer restaurantId
    ) {
        //TODO: 반환값이 null일 경우(해당 식당의 status가 ACTIVE가 아닐 경우) 처리 해줘야함.
        List<RestaurantMenu> restaurantMenus = restaurantService.getRestaurantMenuList(restaurantId);

        return new ResponseEntity<>(restaurantMenus, HttpStatus.OK);
    }

    // 식당 댓글 작성
    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PostMapping("/api/restaurants/{restaurantId}/comments")
    public ResponseEntity<String> postRestaurantComment(
            @PathVariable Integer restaurantId,
            @RequestBody Map<String, Object> jsonBody,
            Principal principal
    ) {
        String result = restaurantCommentService.addComment(
                restaurantId,
                principal.getName(),
                jsonBody.get("commentBody").toString());
        if (result.equals("ok")) {
            return ResponseEntity.ok("Comment added successfully");
        } else if (result.equals("userTokenId")) {
            return ResponseEntity.ok("UserTokenId doesn't exist");
        } else {
            return ResponseEntity.ok("what");
        }
    }

    // 식당 댓글 로드
    @GetMapping("/api/restaurants/{restaurantId}/comments")
    public ResponseEntity<List<Object[]>> getRestaurantCommentByRestaurantId(
            @PathVariable Integer restaurantId,
            @RequestParam(value = "sort", defaultValue = "POPULAR") String sort
    ) {
        SortComment sortComment = SortComment.valueOf(sort);
        List<Object[]> restaurantComments = restaurantCommentService.getCommentList(restaurantId, sortComment);
        return new ResponseEntity<>(restaurantComments, HttpStatus.OK);
    }

    // 식당 댓글 좋아요
    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @GetMapping("/api/restaurants/comments/{commentId}/like")
    public ResponseEntity<Map<String, String>> likeRestaurantComment(
            @PathVariable Integer commentId,
            Principal principal
    ) {
        Map<String, String> responseMap = new HashMap<>();
        try {
            User user = customOAuth2UserService.getUser(principal.getName());
            RestaurantComment restaurantComment = restaurantCommentService.getComment(commentId);

            restaurantCommentService.likeComment(user, restaurantComment, responseMap);
        } catch (DataNotFoundException e) {
            logger.error("RestaurantCommentLikeError", e);
            responseMap.put("status", "error");
            responseMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        } catch (IllegalStateException e) {
            logger.error("RestaurantCommentLikeError", e);
            responseMap.put("status", "error");
            responseMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
        return ResponseEntity.ok(responseMap);
    }

    // 식당 댓글 싫어요
    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @GetMapping("/api/restaurants/comments/{commentId}/dislike")
    public ResponseEntity<Map<String, String>> dislikeRestaurantComment(
            @PathVariable Integer commentId,
            Principal principal
    ) {
        Map<String, String> responseMap = new HashMap<>();
        try {
            User user = customOAuth2UserService.getUser(principal.getName());
            RestaurantComment restaurantComment = restaurantCommentService.getComment(commentId);

            restaurantCommentService.dislikeComment(user, restaurantComment, responseMap);
        } catch (DataNotFoundException e) {
            logger.error("RestaurantCommentDislikeError", e);
            responseMap.put("status", "error");
            responseMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        } catch (IllegalStateException e) {
            logger.error("RestaurantCommentDislikeError", e);
            responseMap.put("status", "error");
            responseMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
        return ResponseEntity.ok(responseMap);
    }

    // 식당 즐겨찾기
    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PostMapping("/api/restaurants/{restaurantId}/favorite/toggle")
    public ResponseEntity<String> toggleFavorite(
            @PathVariable Integer restaurantId,
            Principal principal
    ) {
        String returnValue = restaurantFavoriteService.toggleFavorite(principal.getName(), restaurantId);
        if (returnValue.equals("fail")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else {
            return ResponseEntity.ok(returnValue);
        }
    }
}