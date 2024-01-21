package com.site.restauranttier.controller;

import com.site.restauranttier.entity.*;
import com.site.restauranttier.etc.JsonData;
import com.site.restauranttier.repository.EvaluationRepository;
import com.site.restauranttier.repository.RestaurantRepository;
import com.site.restauranttier.repository.SituationRepository;
import com.site.restauranttier.repository.UserRepository;
import com.site.restauranttier.service.RestaurantCommentService;
import com.site.restauranttier.service.RestaurantService;
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
        logger.info("데이터 확인");
        Restaurant restaurant = restaurantRepository.findByRestaurantId(jsonData.getRestaurantId());
        Optional<User> userOptional = userRepository.findByUserTokenId(principal.getName());

        if (userOptional.isPresent() && restaurant != null) {
            User user = userOptional.get();

            // 기존 평가를 찾습니다.
            Optional<Evaluation> existingEvaluation = evaluationRepository.findByUserAndRestaurant(user, restaurant);
            Evaluation evaluation;
            if (existingEvaluation.isPresent()) {
                // 기존 평가가 존재하면, 업데이트합니다.
                evaluation = existingEvaluation.get();
                evaluation.setEvaluationScore((double) jsonData.getStarRating());
                updateEvaluationItemScores(jsonData, evaluation);

            } else {
                // 새로운 평가를 생성합니다.
                evaluation = new Evaluation(restaurant, user, (double) jsonData.getStarRating());
                restaurant.getEvaluationList().add(evaluation);
                List scoreList=new ArrayList();

                // 상황1부터 9까지 score 만들고 evalution과 situation에 각각 관계 저장
                for(int i=1;i<10;i++){
                    Optional<Situation> situationOptional= situationRepository.findById(i);
                    Situation situation = situationOptional.get();
                    EvaluationItemScore score =  new EvaluationItemScore(evaluation,situation,(double) jsonData.getBarRatings().get(i-1));
                    scoreList.add(score);
                    situation.getEvaluationItemScoreList().add(score);
                }
                evaluation.setEvaluationItemScoreList(scoreList);
            }
            evaluationRepository.save(evaluation);
            // 리다이렉트 대신에 성공 응답을 보냅니다.
            return ResponseEntity.ok("평가가 성공적으로 저장되었습니다.");
        }

        // 에러 메시지를 클라이언트에게 전달합니다.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("오류 발생");
    }
    // 상황 점수를 업데이트하는 메소드 (이해 못함)
    private void updateEvaluationItemScores(JsonData jsonData, Evaluation evaluation) {
        List<Integer> barRatings = jsonData.getBarRatings();
        for (int i = 0; i < barRatings.size(); i++) {
            int situationId = i + 1; // 인덱스에 1을 더해 상황 ID를 얻습니다.
            double scoreValue = barRatings.get(i);

            // 해당 상황 ID로 저장된 EvaluationItemScore를 찾습니다.
            Optional<EvaluationItemScore> scoreOptional = evaluation.getEvaluationItemScoreList()
                    .stream()
                    .filter(score -> score.getSituation().getSituationId() == situationId)
                    .findFirst();

            if (scoreOptional.isPresent()) {
                // 존재하면 업데이트
                EvaluationItemScore score = scoreOptional.get();
                score.setScore(scoreValue);
            } else {
                // 존재하지 않으면 새로운 상황 점수를 추가
                Situation situation = situationRepository.findById(situationId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Situation not found"));
                EvaluationItemScore newScore = new EvaluationItemScore(evaluation, situation, scoreValue);
                evaluation.getEvaluationItemScoreList().add(newScore);
            }
        }
    }
}
