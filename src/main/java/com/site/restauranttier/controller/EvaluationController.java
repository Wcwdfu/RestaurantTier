package com.site.restauranttier.controller;

import com.site.restauranttier.entity.*;
import com.site.restauranttier.etc.JsonData;
import com.site.restauranttier.repository.RestaurantRepository;
import com.site.restauranttier.service.*;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class EvaluationController {
    private final RestaurantService restaurantService;
    private final EvaluationService evaluationService;
    private final CustomOAuth2UserService customOAuth2UserService;


    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    // 평가 페이지 화면
    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @GetMapping("/evaluation/{restaurantId}")
    public String evaluation(
            Model model,
            Principal principal,
            @PathVariable Integer restaurantId
    ) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        User user = customOAuth2UserService.getUser(principal.getName());
        Evaluation evaluation = evaluationService.getByUserAndRestaurant(user, restaurant);

        if (evaluation != null) {
        List<EvaluationItemScore> evaluationItemScoreList = evaluation.getEvaluationItemScoreList();

//            if (evaluationItemScoreList != null) { //상황별 점수매긴거 log로 찍어본 코드
//                for (EvaluationItemScore itemScore : evaluationItemScoreList) {
//                    Situation situation = itemScore.getSituation();
//                    String situationName = situation.getSituationName();
//                    Integer situationId = situation.getSituationId();
//
//                    Double score = itemScore.getScore();
//                    logger.info("Situation: " + situationName + "SitId: " + situationId + ", Score: " + score);
//                }
//            }

            model.addAttribute("eval",evaluation);
            model.addAttribute("evalItemList",evaluationItemScoreList);
        }

        model.addAttribute("restaurant", restaurant);

        return "evaluation";
    }

    // 평가 데이터 db 저장 (기존 평가 존재 시 업데이트 진행)
    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PostMapping("/api/evaluation")
    public ResponseEntity<?> evaluationDBcreate(@RequestBody JsonData jsonData, Principal principal) {
        evaluationService.createOrUpdate(jsonData, principal);

        return ResponseEntity.ok("평가가 성공적으로 저장되었습니다.");
    }

}
