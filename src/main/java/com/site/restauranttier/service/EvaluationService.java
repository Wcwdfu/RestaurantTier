package com.site.restauranttier.service;

import com.site.restauranttier.dataBundle.RestaurantAverageScoreBundle;
import com.site.restauranttier.dataBundle.CategoryTierBundle;
import com.site.restauranttier.dataBundle.RestaurantTierBundle;
import com.site.restauranttier.entity.*;
import com.site.restauranttier.etc.EnumSituation;
import com.site.restauranttier.etc.JsonData;
import com.site.restauranttier.etc.EnumTier;
import com.site.restauranttier.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final SituationRepository situationRepository;
    private final EvaluationItemScoreRepository evaluationItemScoreRepository;
    private final RestaurantService restaurantService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final SituationService situationService;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantSituationRelationRepository restaurantSituationRelationRepository;

    @Value("${tier.min.evaluation}")
    private int minNumberOfEvaluations;

    public Evaluation getByUserAndRestaurant(User user, Restaurant restaurant) {
        Optional<Evaluation> evaluation = evaluationRepository.findByUserAndRestaurant(user, restaurant);
        return evaluation.orElse(null);
    }

    public void createOrUpdate(JsonData jsonData, Principal principal) {

        Restaurant restaurant = restaurantService.getRestaurant(jsonData.getRestaurantId());
        User user = customOAuth2UserService.getUser(principal.getName());

        // user와 restaurant 정보로 db에 평가한 데이터가 있는지 확인
        Optional<Evaluation> evaluationOptional = evaluationRepository.findByUserAndRestaurant(user, restaurant);
        Evaluation evaluation;
        // 있으면 업데이트 및 삭제
        if (evaluationOptional.isPresent()) {
            evaluation = evaluationOptional.get();
            // restaurant tbl update
            double preveMainScore = evaluation.getEvaluationScore();
            restaurant.setRestaurantScoreSum(restaurant.getRestaurantScoreSum() - preveMainScore + jsonData.getStarRating());
            // 메인 점수 업데이트
            evaluation.setEvaluationScore(jsonData.getStarRating());
            // 상황 점수 삭제
            List<EvaluationItemScore> evaluationItemScoreList = evaluation.getEvaluationItemScoreList();
            for (EvaluationItemScore itemScore : evaluationItemScoreList) {
                double prevScore = itemScore.getScore();

                Situation situation = itemScore.getSituation();
                situation.getEvaluationItemScoreList().remove(itemScore);
                situationRepository.save(situation);
                evaluationItemScoreRepository.delete(itemScore);

                // restaurant situation relation tbl update
                List<RestaurantSituationRelation> restaurantSituationRelationList = restaurant.getRestaurantSituationRelationList();
                for (RestaurantSituationRelation restaurantSituationRelation: restaurantSituationRelationList) {
                    Situation relationSituation = restaurantSituationRelation.getSituation();
                    if (relationSituation == situation) {
                        restaurantSituationRelation.setDataCount(restaurantSituationRelation.getDataCount() - 1);
                        restaurantSituationRelation.setScoreSum(restaurantSituationRelation.getScoreSum() - prevScore);
                        break;
                    }
                }
            }
            evaluation.getEvaluationItemScoreList().clear();
        }
        // 없으면 새로 평가 데이터 생성
        else {
            evaluation = new Evaluation(restaurant, user, jsonData.getStarRating());
            // restaurant tbl first update
            restaurant.setRestaurantEvaluationCount(restaurant.getRestaurantEvaluationCount() + 1);
            restaurant.setRestaurantScoreSum(restaurant.getRestaurantScoreSum() + jsonData.getStarRating());
        }
        restaurantRepository.save(restaurant);
        evaluationRepository.save(evaluation);

        // 새로운 EvaluationItemScore 생성 및 저장
        List<EvaluationItemScore> evaluationItemScoreList = new ArrayList<>();
        List situationScoreList = jsonData.getBarRatings();
        for (int i = 0; i < situationScoreList.size(); i++) {
            if (situationScoreList.get(i) == null) {
                continue;
            }

            Situation situation = situationService.getSituation(i + 1); // 1인덱싱이라 +1

            EvaluationItemScore evaluationItemScore = new EvaluationItemScore(evaluation, situation, (Double) situationScoreList.get(i));
            evaluationItemScoreRepository.save(evaluationItemScore);

            // EvaluationItemScore -> evaluationItemScore, situation 과 일대다 매핑
            evaluationItemScoreList.add(evaluationItemScore);
            situation.getEvaluationItemScoreList().add(evaluationItemScore);

            // restaurant situation relation tbl insert
            List<RestaurantSituationRelation> restaurantSituationRelationList = restaurant.getRestaurantSituationRelationList();
            boolean isExist = false;
            double newScore = (Double) situationScoreList.get(i);
            for (RestaurantSituationRelation restaurantSituationRelation: restaurantSituationRelationList) {
                Situation relationSituation = restaurantSituationRelation.getSituation();
                if (relationSituation == situation) {
                    restaurantSituationRelation.setDataCount(restaurantSituationRelation.getDataCount() + 1);
                    restaurantSituationRelation.setScoreSum(restaurantSituationRelation.getScoreSum() + newScore);
                    restaurantSituationRelationRepository.save(restaurantSituationRelation);
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                RestaurantSituationRelation restaurantSituationRelation = new RestaurantSituationRelation();
                restaurantSituationRelation.setRestaurant(restaurant);
                restaurantSituationRelation.setSituation(situation);
                restaurantSituationRelation.setDataCount(1);
                restaurantSituationRelation.setScoreSum((Double) situationScoreList.get(i));
                restaurantSituationRelationRepository.save(restaurantSituationRelation);
            }
        }

        evaluation.setEvaluationItemScoreList(evaluationItemScoreList);
        evaluationRepository.save(evaluation);



    }

    public Integer getEvaluationCountByRestaurantId(Restaurant restaurant) {
        return evaluationRepository.countByRestaurant(restaurant);
    }

    public CategoryTierBundle getTierOfRestaurantInCuisine(Restaurant restaurant) {
        List<RestaurantAverageScoreBundle> restaurantAverageScoreBundleList = restaurantService.getRestaurantAverageScoreBundleListByCuisine(restaurant.getRestaurantCuisine());
        EnumTier tier = EnumTier.calculateTierOfRestaurantInList(restaurantAverageScoreBundleList, restaurant);

        return new CategoryTierBundle(restaurant.getRestaurantCuisine(), tier);
    }

    public List<RestaurantTierBundle> getAllRestaurantTierBundleList() {
        List<RestaurantAverageScoreBundle> restaurantAverageScoreBundleList = restaurantService.getAllRestaurantAverageScoreBundleList();
        List<RestaurantTierBundle> restaurantTierBundleList = new ArrayList<>();
        for (RestaurantAverageScoreBundle restaurantAverageScoreBundle : restaurantAverageScoreBundleList) {
            EnumTier newTier = EnumTier.calculateTierOfRestaurant(restaurantAverageScoreBundle.getAverageScore());
            RestaurantTierBundle restaurantTierBundle = new RestaurantTierBundle(
                    restaurantAverageScoreBundle.getRestaurant(),
                    newTier
            );
            restaurantTierBundleList.add(restaurantTierBundle);
        }
        return restaurantTierBundleList;
    }
    public List<RestaurantTierBundle> getRestaurantTierBundleListByCuisine(String cuisine) {
        List<RestaurantAverageScoreBundle> restaurantAverageScoreBundleList = restaurantService.getRestaurantAverageScoreBundleListByCuisine(cuisine);
        List<RestaurantTierBundle> restaurantTierBundleList = new ArrayList<>();
        for (RestaurantAverageScoreBundle restaurantAverageScoreBundle : restaurantAverageScoreBundleList) {
            RestaurantTierBundle restaurantTierBundle = new RestaurantTierBundle(
                    restaurantAverageScoreBundle.getRestaurant(),
                    EnumTier.calculateTierOfRestaurant(restaurantAverageScoreBundle.getAverageScore())
            );
            restaurantTierBundleList.add(restaurantTierBundle);
        }
        return restaurantTierBundleList;
    }

    public List<RestaurantTierBundle> getRestaurantTierBundleListBySituation(Situation situationObject) {
        List<RestaurantAverageScoreBundle> restaurantAverageScoreBundleList = restaurantService.getRestaurantAverageScoreBundleListBySituation(situationObject);

        return getRestaurantTierBundleList(restaurantAverageScoreBundleList);
    }

    public List<RestaurantTierBundle> getRestaurantTierBundleListByCuisineAndSituation(String cuisine, Situation situationObject) {
        List<RestaurantAverageScoreBundle> restaurantAverageScoreBundleList = restaurantService.getRestaurantAverageScoreBundleListByCuisineAndSituation(cuisine, situationObject);

        return getRestaurantTierBundleList(restaurantAverageScoreBundleList);
    }

    private List<RestaurantTierBundle> getRestaurantTierBundleList(List<RestaurantAverageScoreBundle> restaurantAverageScoreBundleList) {
        List<RestaurantTierBundle> restaurantTierBundleList = new ArrayList<>();
        for (RestaurantAverageScoreBundle restaurantAverageScoreBundle : restaurantAverageScoreBundleList) {
            Restaurant restaurant = restaurantAverageScoreBundle.getRestaurant();
            EnumTier enumTier;
            if (restaurant.getRestaurantEvaluationCount() < minNumberOfEvaluations) {
                enumTier = EnumTier.NONE;
            } else {
                enumTier = EnumTier.calculateTierOfRestaurant(restaurant.getRestaurantScoreSum() / restaurant.getRestaurantEvaluationCount());
            }
            RestaurantTierBundle restaurantTierBundle = new RestaurantTierBundle(
                    restaurantAverageScoreBundle.getRestaurant(),
                    enumTier
            );
            restaurantTierBundleList.add(restaurantTierBundle);
        }
        return restaurantTierBundleList;
    }
}

