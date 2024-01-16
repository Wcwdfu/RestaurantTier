package com.site.restauranttier.service;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.RestaurantHashtag;
import com.site.restauranttier.entity.SituationCategory;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private Specification<Restaurant> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            // 검색 로직 구현 ( 식당 이름, 해시태그 이름, 카테고리 이름 기준)
            @Override
            public Predicate toPredicate(Root<Restaurant> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Restaurant, RestaurantHashtag> hashtagJoin  = q.join("restaurantHashtagList", JoinType.LEFT);
                Join<Restaurant, SituationCategory> categoryJoin  = q.join("SituationCategoryList", JoinType.LEFT);
                Predicate predicate = cb.or(
                        cb.like(q.get("restaurantName"), "%" + kw + "%"),   // 레스토랑 이름
                        cb.like(hashtagJoin.get("hashtagName"), "%" + kw + "%"),  // 레스토랑 해시태그
                        cb.like(categoryJoin.get("categoryName"), "%" + kw + "%")  // 상황 카테고리
                );
                return predicate;
            }
        };
    }

    public Page<Restaurant> getList(int page,String kw) {
        // 여기서 티어 순으로 정렬해서 가져올 수 있음, 아래는 만들진 순으로 정렬
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10);
        Specification<Restaurant> spec=search(kw);
        return this.restaurantRepository.findAll(spec,pageable);
    }

}
