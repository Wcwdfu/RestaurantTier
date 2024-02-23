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

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class RecommendController {
    private final RestaurantService restaurantService;
    private static final Logger logger = LoggerFactory.getLogger(RecommendController.class);

    // 메뉴추천 화면
    @GetMapping("/recommend")
    public String recommend(Model model) {
        List<Restaurant> restaurants = restaurantService.getTopRestaurantsByCuisine();
        model.addAttribute("restaurants", restaurants);

        model.addAttribute("currentPage", "recommend");
        List<String> cuisines = new ArrayList<>(Arrays.asList("전체","한식","일식","중식","양식","아시안","고기","치킨","술집","햄버거","해산물","분식","샐러드","카페","베이커리","기타"));
        model.addAttribute("cuisines", cuisines);
        return "recommend";
    }

    // 메뉴 리스트 받아오기
    @GetMapping("/api/recommend")
    public ResponseEntity<List<Restaurant>> getRestaurantListForCuisine(
            @RequestParam(value = "cuisine", defaultValue = "전체") String cuisine, @RequestParam(value = "location",defaultValue = "전체") String location, @RequestParam(value = "evaluation",defaultValue = "전체") String evaluation
    ) {
        String[] cuisinesArray = cuisine.split("-");
        List<String> cuisinesList = Arrays.asList(cuisinesArray);
        List<Restaurant> combinedRestaurantList = new ArrayList<>();
        // 음식 종류마다 location 과 일치하는 식당 리스트 반환해서 combinedRestaurantList에 추가
        for (String item : cuisinesList) {
            List<Restaurant> retaurantList = restaurantService.getRestaurantListByRandomPick(item,location);
            combinedRestaurantList.addAll(retaurantList);
        }

        // 조건에 맞게 정렬, 전체면 그대로
        if (evaluation.equals("평점 높은 순")) {
            combinedRestaurantList = combinedRestaurantList.stream()
                    .sorted(Comparator.comparingDouble((Restaurant r) -> r.getRestaurantScoreSum() / (double) r.getRestaurantEvaluationCount()).reversed())
                    .collect(Collectors.toList());
        } else if (evaluation.equals("후기 많은 순")) {
            combinedRestaurantList = combinedRestaurantList.stream()
                    .sorted(Comparator.comparingInt((Restaurant r) -> r.getEvaluationList().size()).reversed())
                    .collect(Collectors.toList());
        }
        // 조건에 맞게 상위 100개 선택
        List<Restaurant> top100List = combinedRestaurantList.stream().limit(100).collect(Collectors.toList());

        // 상위 100개 중에서 랜덤으로 30개 선택
        return new ResponseEntity<>(getRandomSubList(top100List, 30), HttpStatus.OK);

    }

    // 랜덤으로 섞은 후 정확히 target 변수 만큼 의 식당을 반환하는 메소드
    private List<Restaurant> getRandomSubList(List<Restaurant> originalList, int targetSize) {
        List<Restaurant> resultList = new ArrayList<>(originalList.size());
        Random rand = new Random();

        // 원본 리스트가 targetSize보다 작으면, 원본 리스트의 항목을 반복하여 추가
        while (resultList.size() < targetSize) {
            if (!originalList.isEmpty()) {
                resultList.addAll(originalList);
            } else {
                // 주어진 originalList가 비어있으면 빈 resultList 반환
                return resultList;
            }
        }

        // resultList를 랜덤으로 섞음
        Collections.shuffle(resultList, rand);

        // 결과 리스트에서 targetSize만큼 잘라서 반환
        return new ArrayList<>(resultList.subList(0, targetSize));
    }
    // id에 해당 하는 식당 정보 반환
    @GetMapping("/api/recommend/restaurant")
    public ResponseEntity<Restaurant> recommendRestaurant(@RequestParam(name = "restaurantId") String restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurant(Integer.valueOf(restaurantId));
        return ResponseEntity.ok(restaurant);
    }

}
