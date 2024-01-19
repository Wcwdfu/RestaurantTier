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

            @Override
            public Predicate toPredicate(Root<Restaurant> r, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Restaurant, RestaurantHashtag> u1 = r.join("restaurantHashtagList", JoinType.LEFT);
                Join<Restaurant, Situation> u2 = r.join("situationList", JoinType.LEFT);
                return cb.or(cb.like(r.get("restaurantName"), "%" + kw + "%"), // 식당 이름
                        cb.like(r.get("restaurantType"), "%" + kw + "%"),      // 식당 카테고리 (ex.곰탕)
                        cb.like(r.get("restaurantCuisine"), "%" + kw + "%"),    // 식당 종류 (ex.한식)
                        cb.like(u1.get("hashtagName"), "%" + kw + "%"),      // 해시태그 이름
                        cb.like(u2.get("situationName"), "%" + kw + "%"));   // 상황 이름
            }
        };
    }

    // 페이지 번호를 입력받아 해당 페이지의 데이터 조회
    public Page<Restaurant> getList(int page) {
        Pageable pageable = PageRequest.of(page, 30);
        return this.restaurantRepository.findAll(pageable);
    }

    // 페이지 번호를 입력받아 해당 페이지의 데이터 조회
    public Page<Restaurant> getList(int page,String kw) {
        Pageable pageable = PageRequest.of(page, 30);
        Specification<Restaurant> spec = search(kw);

        return this.restaurantRepository.findAll(spec,pageable);
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
