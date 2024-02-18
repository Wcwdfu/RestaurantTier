package com.site.restauranttier.service;

import com.site.restauranttier.DataNotFoundException;
import com.site.restauranttier.entity.*;
import com.site.restauranttier.repository.RestaurantMenuRepository;
import com.site.restauranttier.repository.RestaurantRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMenuRepository restaurantmenuRepository;

    @Value("${tier.min.evaluation}")
    private int minNumberOfEvaluations;

    private Specification<Restaurant> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Restaurant> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거

                // 조인
                Join<Restaurant, RestaurantHashtag> joinHashtag = root.join("restaurantHashtagList", JoinType.LEFT);
                Join<Restaurant, Situation> joinSituation = root.join("situationList", JoinType.LEFT);

                // 검색 조건
                Predicate namePredicate = cb.like(root.get("restaurantName"), "%" + kw + "%");
                Predicate typePredicate = cb.like(root.get("restaurantType"), "%" + kw + "%");
                Predicate cuisinePredicate = cb.like(root.get("restaurantCuisine"), "%" + kw + "%");
                Predicate hashtagPredicate = cb.like(joinHashtag.get("hashtagName"), "%" + kw + "%");
                Predicate situationPredicate = cb.like(joinSituation.get("situationName"), "%" + kw + "%");

                // 'ACTIVE' 상태 조건 추가
                Predicate statusPredicate = cb.equal(root.get("status"), "ACTIVE");

                // 모든 조건을 결합
                return cb.and(statusPredicate, cb.or(namePredicate, typePredicate, cuisinePredicate, hashtagPredicate, situationPredicate));
            }
        };
    }

    public Restaurant getRestaurant(Integer id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isPresent()) {
            return restaurant.get();
        } else {
            throw new DataNotFoundException("restaurant not found");
        }

    }


    // 페이지 번호를 입력받아 해당 페이지의 데이터 조회
    public Page<Restaurant> getList(int page, String kw) {
        Pageable pageable = PageRequest.of(page, 30);
        Specification<Restaurant> spec = search(kw);

        return restaurantRepository.findAll(spec, pageable);
    }
    // 식당의 메뉴 리스트 반환
    public List<RestaurantMenu> getRestaurantMenuList(int restaurantId) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if (restaurant.getStatus().equals("ACTIVE")) {
            return restaurantmenuRepository.findByRestaurantOrderByMenuId(restaurant);
        } else {
            return null;
        }
    }
    // status가 ACTIVE인 cuisine에 속한 식당 리스트 반환
    public List<Restaurant> getRestaurantList(String cuisine) {
        if (cuisine.equals("전체")) {
            return restaurantRepository.findByStatus("ACTIVE");
        } else {
            return restaurantRepository.findByRestaurantCuisineAndStatus(cuisine, "ACTIVE");
        }
    }
    // 뽑기 리스트 반환
    // location이 전체면 그냥 반환
    public List<Restaurant> getRestaurantListByRandomPick(String cuisine, String location) {
        if(location.equals("전체")) {
            return restaurantRepository.findByRestaurantCuisineAndStatus(cuisine, "ACTIVE");
        }
        return restaurantRepository.findByRestaurantCuisineAndStatusAndRestaurantPosition(cuisine, "ACTIVE", location);
    }
    // 해당 식당이 방문 상위 몇 퍼센트인지 반환
    public Float getPercentOrderByVisitCount(Restaurant restaurant) {
        return restaurantRepository.getPercentOrderByVisitCount(restaurant);
    }
    // 식당 방문 카운트 1 증가
    public void plusVisitCount(Restaurant restaurant) {
        restaurant.setVisitCount(restaurant.getVisitCount() + 1);
        restaurantRepository.save(restaurant);
    }

    // 인기 식당 반환 (모두 0이면 db의 가장 처음 요소 뽑힘
    public List<Restaurant> getTopRestaurantsByCuisine() {
        // 모든 식당을 불러온다. 실제로는 repository에서 findAll()을 사용하거나,
        // 필요한 데이터만 가져오는 쿼리 메서드를 정의하여 사용
        List<Restaurant> restaurants = restaurantRepository.findByStatus("ACTIVE");

        // 각 식당의 평균 평가 점수를 기준으로 Cuisine 별로 최고 점수의 식당을 찾는다.
        Map<String, Optional<Restaurant>> topRestaurantsByCuisine = restaurants.stream()
                .collect(Collectors.groupingBy(
                        Restaurant::getRestaurantCuisine,
                        Collectors.maxBy(Comparator.comparingDouble(r ->
                                r.getEvaluationList().stream()
                                        .mapToDouble(Evaluation::getEvaluationScore)
                                        .average()
                                        .orElse(0)
                        ))
                ));

        // Optional<Restaurant>을 처리하여 실제 Restaurant 객체만 필터링하고 리스트로 반환
        return topRestaurantsByCuisine.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
