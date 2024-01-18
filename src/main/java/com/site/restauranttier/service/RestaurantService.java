package com.site.restauranttier.service;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.RestaurantHashtag;
import com.site.restauranttier.entity.RestaurantMenu;
import com.site.restauranttier.entity.Situation;
import com.site.restauranttier.enums.SortComment;
import com.site.restauranttier.repository.RestaurantMenuRepository;
import com.site.restauranttier.repository.RestaurantRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMenuRepository restaurantmenuRepository;
    private Specification<Restaurant> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            // 검색 로직 구현 ( 식당 이름, 해시태그 이름, 카테고리 이름 기준)
            @Override
            public Predicate toPredicate(Root<Restaurant> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Restaurant, RestaurantHashtag> hashtagJoin  = q.join("restaurantHashtagList", JoinType.LEFT);
                Join<Restaurant, Situation> categoryJoin  = q.join("SituationCategoryList", JoinType.LEFT);
                Predicate predicate = cb.or(
                        cb.like(q.get("restaurantName"), "%" + kw + "%"),   // 레스토랑 이름
                        cb.like(hashtagJoin.get("hashtagName"), "%" + kw + "%"),  // 레스토랑 해시태그
                        cb.like(categoryJoin.get("categoryName"), "%" + kw + "%")  // 상황 카테고리
                );
                return predicate;
            }
        };
    }
    // 페이지 번호를 입력받아 해당 페이지의 데이터 조회
    public Page<Restaurant> getList(int page) {
        Pageable pageable = PageRequest.of(page, 30);
        return this.restaurantRepository.findAll(pageable);
    }

    public List<RestaurantMenu> getRestaurantMenuList(int restaurantId) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if (restaurant.getStatus().equals("ACTIVE")) {
            return restaurantmenuRepository.findByRestaurantOrderByMenuId(restaurant);
        } else {
            return null;
        }
    }
}
