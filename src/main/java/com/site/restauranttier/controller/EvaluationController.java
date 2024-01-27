package com.site.restauranttier.controller;

import com.site.restauranttier.entity.*;
import com.site.restauranttier.etc.JsonData;
import com.site.restauranttier.repository.*;
import com.site.restauranttier.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class EvaluationController {


    private final RestaurantService restaurantService;
    private final UserService userService;
    private final EvaluationRepository evaluationRepository;
    private final EvaluationService evaluationService;
    private final EvaluationItemScoreService evaluationItemScoreService;
    private final SituationRepository situationRepository;
    private final EvaluationItemScoreRepository evaluationItemScoreRepository;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    // 평가 페이지
    @GetMapping("/evaluation/{restaurantId}")
    public String evaluation(Model model, @PathVariable Integer restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "evaluation";
    }

    // 평가 데이터 db 저장 (한 user의 똑같은 식당이면 업데이트 진행)
    @PostMapping("/api/evaluation")
    public ResponseEntity<?> evaluationDBcreate(@RequestBody JsonData jsonData, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        Restaurant restaurant = restaurantService.getRestaurant(jsonData.getRestaurantId());
        User user = userService.getUser(principal.getName());

        // user와 restaurant 정보로 평가 db에 평가한 데이터가 있는지 확인
        Evaluation evaluation = evaluationService.getByUserAndRestaurant(user, restaurant);
        // 있으면 업데이트 및 삭제
        if (evaluation != null) {
            // 메인 점수 업데이트
            evaluation.setEvaluationScore(jsonData.getStarRating());
            // 상황 점수 삭제
            evaluationService.deleteItemScoresAll(evaluation);
        }
        // 없으면 새로 평가 데이터 생성
        else {
            evaluation = new Evaluation(restaurant, user, jsonData.getStarRating());
        }
        evaluationRepository.save(evaluation);

        // 새로운 EvaluationItemScore 생성 및 저장
        List<EvaluationItemScore> evaluationItemScoreList = new ArrayList<>();
        List situationScoreList = jsonData.getBarRatings();
        for (int i = 0; i < situationScoreList.size(); i++) {
            if (situationScoreList.get(i) == null) {
                continue;
            }

            Optional<Situation> situationOptional = situationRepository.findById(i + 1); // 1인덱싱이라 +1
            Situation situation = situationOptional.get();

            EvaluationItemScore evaluationItemScore = new EvaluationItemScore(evaluation, situation, (Double) situationScoreList.get(i));
            evaluationItemScoreRepository.save(evaluationItemScore);

            // EvaluationItemScore -> evaluationItemScore, situation 과 일대다 매핑
            evaluationItemScoreList.add(evaluationItemScore);
            situation.getEvaluationItemScoreList().add(evaluationItemScore);
        }

        evaluation.setEvaluationItemScoreList(evaluationItemScoreList);
        evaluationRepository.save(evaluation);

        return ResponseEntity.ok("평가가 성공적으로 저장되었습니다.");


    }


}
