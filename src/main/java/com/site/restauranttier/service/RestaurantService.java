package com.site.restauranttier.service;

import com.site.restauranttier.DataNotFoundException;
import com.site.restauranttier.dataBundle.RestaurantAverageScoreBundle;
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

import java.util.List;
import java.util.Optional;

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

    public List<RestaurantMenu> getRestaurantMenuList(int restaurantId) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if (restaurant.getStatus().equals("ACTIVE")) {
            return restaurantmenuRepository.findByRestaurantOrderByMenuId(restaurant);
        } else {
            return null;
        }
    }

    public List<Restaurant> getRestaurantList(String cuisine) {
        if (cuisine.equals("전체")) {
            return restaurantRepository.findByStatus("ACTIVE");
        } else {
            return restaurantRepository.findByRestaurantCuisineAndStatus(cuisine, "ACTIVE");
        }
    }

    public Float getPercentOrderByVisitCount(Restaurant restaurant) {
        return restaurantRepository.getPercentOrderByVisitCount(restaurant);
    }

    public void plusVisitCount(Restaurant restaurant) {
        restaurant.setVisitCount(restaurant.getVisitCount() + 1);
        restaurantRepository.save(restaurant);
    }

    public List<RestaurantAverageScoreBundle> getAllRestaurantAverageScoreBundleList() {
        return restaurantRepository.getAllRestaurantsOrderedByAvgScore(minNumberOfEvaluations);
    }
    public List<RestaurantAverageScoreBundle> getCuisineRestaurantAverageScoreBundleList(String cuisine) {
        return restaurantRepository.getRestaurantsByCuisineOrderedByAvgScore(cuisine, minNumberOfEvaluations);
    }
}
