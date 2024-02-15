package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class RecommendController {
    private final RestaurantService restaurantService;
    private static final Logger logger = LoggerFactory.getLogger(RecommendController.class);
    // 메뉴추천 화면
    @GetMapping("/recommend")
    public String recommend(Model model){
        model.addAttribute("currentPage","recommend");

        return "recommend";
    }

    // 메뉴 리스트 받아오기
    // 한식, 일식, 중식
    // /api/recommend?cuisine=한식-일식-중식
    @GetMapping("/api/recommend")
    public ResponseEntity<List<Restaurant>> getRestaurantListForCuisine(
            @RequestParam(value = "cuisine", defaultValue = "전체") String cuisine
    ) {
        if (cuisine.equals("전체")) {
            List<Restaurant> retaurantList = restaurantService.getRestaurantList("전체");
            return new ResponseEntity<>(retaurantList, HttpStatus.OK);
        } else {
            String[] cuisinesArray = cuisine.split("-");
            List<String> cuisinesList = Arrays.asList(cuisinesArray);

            List<Restaurant> combinedRestaurantList = new ArrayList<>();
            for (String item : cuisinesList) {
                List<Restaurant> retaurantList = restaurantService.getRestaurantList(item);
                combinedRestaurantList.addAll(retaurantList);
            }

            // restaurantImgUrl이 없는 객체에 대해 기본 이미지 URL 설정
            combinedRestaurantList.forEach(restaurant -> {
                if (restaurant.getRestaurantImgUrl() == null || restaurant.getRestaurantImgUrl().isEmpty()) {
                    restaurant.setRestaurantImgUrl("/path/to/no_img.png"); // 기본 이미지 경로 설정
                }
            });
            return new ResponseEntity<>(combinedRestaurantList, HttpStatus.OK);
        }
    }
    // id에 해당 하는 식당 정보 반환
    @GetMapping("/recommend/restaurant")
    public ResponseEntity<Restaurant> recommendRestaurant(@RequestParam(name="restaurantId") String restaurantId) {
        logger.info(restaurantId);
        Restaurant restaurant =restaurantService.getRestaurant(Integer.valueOf(restaurantId));
        return ResponseEntity.ok(restaurant);
    }

}
