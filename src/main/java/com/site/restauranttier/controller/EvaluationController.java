package com.site.restauranttier.controller;

import com.site.restauranttier.entity.*;
import com.site.restauranttier.etc.JsonData;
import com.site.restauranttier.repository.*;
import com.site.restauranttier.service.RestaurantCommentService;
import com.site.restauranttier.service.RestaurantService;
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

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    private final EvaluationRepository evaluationRepository;
    private final SituationRepository situationRepository;
    private final EvaluationItemScoreRepository evaluationItemScoreRepository;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    // 평가 페이지
    @GetMapping("/evaluation/{restaurantId}")
    public String evaluation(Model model, @PathVariable Integer restaurantId) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "evaluation";
    }

    // 평가 데이터 db 저장 (한 user의 똑같은 식당이면 업데이트 진행)
    @PostMapping("/api/evaluation")
    public ResponseEntity<?> evaluationDBcreate(@RequestBody JsonData jsonData, Principal principal) {
        if(principal==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        Restaurant restaurant = restaurantRepository.findByRestaurantId(jsonData.getRestaurantId());
        Optional<User> userOptional = userRepository.findByUserTokenId(principal.getName());

        if (userOptional.isPresent() && restaurant != null) {
            User user = userOptional.get();
            Optional<Evaluation> existingEvaluation = evaluationRepository.findByUserAndRestaurant(user, restaurant);
            Evaluation evaluation;

            if (existingEvaluation.isPresent()) {
                evaluation = existingEvaluation.get();
                evaluation.setEvaluationScore( jsonData.getStarRating());

                // 기존 EvaluationItemScore 삭제 및 Evaluation과 Situation 업데이트
                for (EvaluationItemScore itemScore : evaluation.getEvaluationItemScoreList()) {
                    // situation 업뎃
                    Situation situation = itemScore.getSituation();
                    situation.getEvaluationItemScoreList().remove(itemScore);
                    situationRepository.save(situation);
                    // 기존 EvaluationItemScore 삭제
                    evaluationItemScoreRepository.delete(itemScore);
                }
                // evaluation 업뎃
                evaluation.getEvaluationItemScoreList().clear();
            } else {
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

                Optional<Situation> situationOptional = situationRepository.findById(i+1); // 1인덱싱이라 +1
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("오류 발생");
    }



}
