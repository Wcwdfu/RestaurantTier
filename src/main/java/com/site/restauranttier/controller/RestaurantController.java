package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Evaluation;
import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.RestaurantMenu;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.EvaluationRepository;
import com.site.restauranttier.repository.RestaurantRepository;
import com.site.restauranttier.repository.SituationRepository;
import com.site.restauranttier.repository.UserRepository;
import com.site.restauranttier.service.RestaurantCommentService;
import com.site.restauranttier.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class RestaurantController {
    private final RestaurantCommentService restaurantCommentService;
    private final EvaluationRepository evaluationRepository;
    private final SituationRepository situationRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;
    
    @Value("${restaurant.initialDisplayMenuCount}")
    private int initialDisplayMenuCount;

    @GetMapping("/restaurants/{restaurantId}")
    public String restaurant(Model model,
                             @PathVariable Integer restaurantId, Principal principal
    ) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        model.addAttribute("restaurant", restaurant);

        List<RestaurantMenu> restaurantMenus = restaurantService.getRestaurantMenuList(restaurantId);
        model.addAttribute("menus", restaurantMenus);

        model.addAttribute("initialDisplayMenuCount", initialDisplayMenuCount);
        // 해당 식당 평가한 적 있으면 버튼 이름 변경 (다시 평가하기)
        // 로그인 안되어있을 경우
        if (principal == null) {
            model.addAttribute("evaluationButton", " 평가하기");
            return "restaurant";
        } else {
            String name = principal.getName();
            User user = userRepository.findByUserTokenId(name).orElseThrow();
            Optional<Evaluation> evaluationOpt = evaluationRepository.findByUserAndRestaurant(user, restaurant);
            if (evaluationOpt.isPresent()) {
                model.addAttribute("evaluationButton", "다시 평가하기");
            } else {
                model.addAttribute("evaluationButton", " 평가하기");

            }
            return "restaurant";
        }

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
    //TODO: 안됨 다시 해야됨.
    @PostMapping("/api/restaurants/{restaurantId}/comments")
    public ResponseEntity<String> postRestaurantComment(
            @PathVariable Integer restaurantId,
            @RequestBody Map<String, Object> jsonBody
    ) {
        String result = restaurantCommentService.addComment(
                restaurantId,
                jsonBody.get("userTokenId").toString(),
                jsonBody.get("commentBody").toString());

        if (result.equals("ok")) {
            return ResponseEntity.ok("Comment added successfully");
        } else if (result.equals("userTokenId")) {
            return ResponseEntity.ok("UserTokenId doesn't exist");
        } else {
            return ResponseEntity.ok("what");
        }
    }
}
