package com.site.restauranttier.service;

import com.site.restauranttier.entity.*;
import com.site.restauranttier.etc.JsonData;
import com.site.restauranttier.etc.EnumTier;
import com.site.restauranttier.etc.RestaurantTierDataClass;
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

        // 메인(cuisine) 티어 저장
        if (restaurant.getRestaurantEvaluationCount() >= minNumberOfEvaluations) {
            EnumTier tier = EnumTier.calculateTierOfRestaurant(restaurant.getRestaurantScoreSum() / restaurant.getRestaurantEvaluationCount());
            restaurant.setMainTier(tier.getValue());
        } else {
            restaurant.setMainTier(-1);
        }
        restaurantRepository.save(restaurant);

        // 상황(situation) 티어 저장
        for (RestaurantSituationRelation restaurantSituationRelation : restaurant.getRestaurantSituationRelationList()) {
            if (restaurantSituationRelation.getDataCount() >= minNumberOfEvaluations) {
                Double AvgScore = restaurantSituationRelation.getScoreSum() / restaurantSituationRelation.getDataCount();
                restaurantSituationRelation.setSituationTier(EnumTier.calculateSituationTierOfRestaurant(AvgScore).getValue());
            } else {
                restaurantSituationRelation.setSituationTier(-1);
            }
            restaurantSituationRelationRepository.save(restaurantSituationRelation);
        }
    }

    public List<RestaurantTierDataClass> getAllRestaurantTierDataClassList() {
        List<Restaurant> restaurantList = restaurantRepository.getAllRestaurantsOrderedByAvgScore(minNumberOfEvaluations);

        return convertToTierDataClassList(restaurantList);
    }

    public List<RestaurantTierDataClass> getRestaurantTierDataClassListByCuisine(String cuisine) {
        List<Restaurant> restaurantList = restaurantRepository.getRestaurantsByCuisineOrderedByAvgScore(cuisine, minNumberOfEvaluations);

        return convertToTierDataClassList(restaurantList);
    }

    public List<RestaurantTierDataClass> getRestaurantTierDataClassListBySituation(Situation situation) {
        List<Restaurant> restaurantList = restaurantRepository.getRestaurantsBySituationOrderedByAvgScore(situation, minNumberOfEvaluations);

        return convertToTierDataClassList(restaurantList, situation);
    }

    public List<RestaurantTierDataClass> getRestaurantTierDataClassListByCuisineAndSituation(String cuisine, Situation situation) {
        List<Restaurant> restaurantList = restaurantRepository.getRestaurantsByCuisineAndSituationOrderedByAvgScore(cuisine, situation, minNumberOfEvaluations);

        return convertToTierDataClassList(restaurantList, situation);
    }

    private List<RestaurantTierDataClass> convertToTierDataClassList(List<Restaurant> restaurantList) {
        List<RestaurantTierDataClass> resultList = new ArrayList<>();
        for (int i = 0; i < restaurantList.size(); i++) {
            Restaurant restaurant = restaurantList.get(i);
            RestaurantTierDataClass newDataClass = new RestaurantTierDataClass(restaurant);
            if (restaurant.getRestaurantEvaluationCount() < minNumberOfEvaluations) { // 평가 데이터 부족
                newDataClass.setRanking("-");
                resultList.add(newDataClass);
            } else { // 평가 데이터가 충분히 있는 경우
                newDataClass.setRanking((i + 1) + "");
                insertSituation(newDataClass, restaurant);
                resultList.add(newDataClass);
            }
        }
        return resultList;
    }

    private List<RestaurantTierDataClass> convertToTierDataClassList(List<Restaurant> restaurantList, Situation situation) {
        List<RestaurantTierDataClass> resultList = new ArrayList<>();
        for (int i = 0; i < restaurantList.size(); i++) {
            Restaurant restaurant = restaurantList.get(i);
            RestaurantTierDataClass newDataClass = new RestaurantTierDataClass(restaurant, getSituationTier(restaurant, situation));
            newDataClass.setRanking((i + 1) + "");
            if (restaurant.getRestaurantEvaluationCount() < minNumberOfEvaluations) { // 평가 데이터 부족
                resultList.add(newDataClass);
            } else { // 평가 데이터가 충분히 있는 경우
                insertSituation(newDataClass, restaurant);
                resultList.add(newDataClass);
            }
        }
        return resultList;
    }

    private Integer getSituationTier(Restaurant restaurant, Situation situation) {
        for (RestaurantSituationRelation restaurantSituationRelation : restaurant.getRestaurantSituationRelationList()) {
            if (restaurantSituationRelation.getSituation().equals(situation)) {
                return restaurantSituationRelation.getSituationTier();
            }
        }
        return -1;
    }

    private void insertSituation(RestaurantTierDataClass newDataClass, Restaurant restaurant) {
        for (RestaurantSituationRelation restaurantSituationRelation : restaurant.getRestaurantSituationRelationList()) {
            if (restaurantSituationRelation.getDataCount() >= minNumberOfEvaluations) {
                newDataClass.addSituation(restaurantSituationRelation.getSituation());
            }
        }
    }
}

