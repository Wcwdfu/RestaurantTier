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
        logger.info("컨트롤러 진입");
        Restaurant restaurant = restaurantRepository.findByRestaurantId(jsonData.getRestaurantId());
        Optional<User> userOptional = userRepository.findByUserTokenId(principal.getName());

        if (userOptional.isPresent() && restaurant != null) {
            User user = userOptional.get();
            // 기존 평가를 찾습니다.
            Optional<Evaluation> existingEvaluation = evaluationRepository.findByUserAndRestaurant(user, restaurant);
            Evaluation evaluation;
            if (existingEvaluation.isPresent()) {
                // 기존 평가가 존재하면, 업데이트
                evaluation = existingEvaluation.get();
                evaluation.setEvaluationScore((double) jsonData.getStarRating());

            } else {
                logger.info("새로운 평가 진입");

                // 새로운 평가를 생성
                evaluation = new Evaluation(restaurant, user, (double) jsonData.getStarRating());
                evaluation = this.evaluationRepository.save(evaluation);

                restaurant.getEvaluationList().add(evaluation);
                List scoreList = new ArrayList();
                List situationScoreList = jsonData.getBarRatings();
                // 각 상황마다 해당하는 점수를 저장
                for (int i = 0; i < situationScoreList.size(); i++) {
                    // 평가되지 않은 상황번호는 넘기기
                    if (situationScoreList.get(i) == null) {
                        logger.info("없는 번호의 상황 넘기기");

                        continue;

                    }
                    Optional<Situation> situationOptional = situationRepository.findById(i);
                    Situation situation = situationOptional.get();

                    // score 생성
                    EvaluationItemScore score = new EvaluationItemScore(evaluation, situation, (Double) situationScoreList.get(i));
                    scoreList.add(score);
                    // score 저장
                    this.evaluationItemScoreRepository.save(score);
                    // situtation 일대다 매핑 -score
                    situation.getEvaluationItemScoreList().add(score);


                }
                // evalutation 일대다 매핑 -score
                evaluation.setEvaluationItemScoreList(scoreList);
                // evaluation 저장
                this.evaluationRepository.save(evaluation);
                logger.info("저장");

            }


            // 리다이렉트 대신에 성공 응답을 보냅니다.
            return ResponseEntity.ok("평가가 성공적으로 저장되었습니다.");
        }

        // 에러 메시지를 클라이언트에게 전달합니다.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("오류 발생");
    }


}
