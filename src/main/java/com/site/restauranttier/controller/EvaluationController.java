package com.site.restauranttier.controller;

import com.google.gson.Gson;
import com.site.restauranttier.entity.*;
import com.site.restauranttier.etc.JsonData;
import com.site.restauranttier.repository.EvaluationRepository;
import com.site.restauranttier.service.*;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Controller
public class EvaluationController {
    private final RestaurantService restaurantService;
    private final EvaluationService evaluationService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final EvaluationRepository evaluationRepository;
    Gson gson = new Gson();

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
        Optional<Evaluation> evaluation = evaluationRepository.findByUserAndRestaurant(user, restaurant);

        Map<Integer, Double> situationEvaluationData = new HashMap<>();
        Double mainScore = 0.0;

        if (evaluation.isPresent()) {
            List<EvaluationItemScore> evaluationItemScoreList = evaluation.get().getEvaluationItemScoreList();
            //상황 데이터 Map에 추가
            for (EvaluationItemScore evaluationItemScore: evaluationItemScoreList) {
                situationEvaluationData.put(evaluationItemScore.getSituation().getSituationId(), evaluationItemScore.getScore());
            }
            mainScore = evaluation.get().getEvaluationScore();
        }
        model.addAttribute("situationJson", gson.toJson(situationEvaluationData));
        model.addAttribute("mainScore", mainScore);
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
