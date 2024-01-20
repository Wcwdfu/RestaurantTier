package com.site.restauranttier.controller;

import com.site.restauranttier.entity.*;
import com.site.restauranttier.etc.JsonData;
import com.site.restauranttier.repository.EvaluationRepository;
import com.site.restauranttier.repository.RestaurantRepository;
import com.site.restauranttier.repository.SituationRepository;
import com.site.restauranttier.repository.UserRepository;
import com.site.restauranttier.service.RestaurantCommentService;
import com.site.restauranttier.service.RestaurantService;
import groovy.util.Eval;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantService restaurantService;
    private final RestaurantCommentService restaurantCommentService;
    private final EvaluationRepository evaluationRepository;
    private final SituationRepository situationRepository;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    // ---------------상단 탭 관련-------------------

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/community")
    public String community() {
        return "community";
    }

    @GetMapping("/ranking")
    public String ranking() {
        return "ranking";
    }

    // 티어표 들어갈 때 기본 값으로 전체 식당 출력
    @GetMapping("/tier")
    public String tier(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "cuisine", required = false) String cuisine) {
        // 메인에서 이미지로 티어표로 이동할 때
        if (cuisine != null && !cuisine.isEmpty() && !"null".equals(cuisine)) {
            Pageable pageable = PageRequest.of(page, 30);
            Page<Restaurant> paging = restaurantRepository.findByRestaurantCuisine(cuisine, pageable);

            model.addAttribute("paging", paging);
            model.addAttribute("cuisine", cuisine);
            return "tier";
        } else {
            //그냥 티어표로 이동할때
            Page<Restaurant> paging = this.restaurantService.getList(page);
            model.addAttribute("paging", paging);
            return "tier";
        }

    }

    // --------------상단 탭 관련 끝---------------------
    @GetMapping("/restaurants/{restaurantId}")
    public String restaurant(Model model,
                             @PathVariable Integer restaurantId
    ) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "restaurant";
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

    // 티어표 안에서 종류 카테고리 누를때 데이터 반환
    @ResponseBody
    @GetMapping("/api/tier")
    public ResponseEntity<List<Restaurant>> getRestaurantsByCuisine(@RequestParam(name = "cuisine", required = false) String cuisine) {
        List<Restaurant> restaurants;
        if (cuisine != null && !cuisine.isEmpty()) {
            // cuisine 파라미터가 주어진 경우, 해당하는 데이터를 조회합니다.
            restaurants = restaurantRepository.findByRestaurantCuisine(cuisine);
        } else {
            // cuisine 파라미터가 없는 경우, 모든 레스토랑을 조회합니다.
            restaurants = restaurantRepository.findAll();
        }

        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

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

    // 검색 결과 페이지
    @GetMapping("/api/search")
    public String search(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Restaurant> paging = this.restaurantService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "searchResult";


    }
}




