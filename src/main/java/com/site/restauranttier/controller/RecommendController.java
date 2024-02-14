package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    // 메뉴추천 화면
    @GetMapping("/recommend")
    public String recommend(Model model){
        List<Restaurant> restaurants = restaurantService.getTopRestaurantsByCuisine();
        model.addAttribute("restaurants",restaurants);
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
            return new ResponseEntity<>(combinedRestaurantList, HttpStatus.OK);
        }
    }
}
