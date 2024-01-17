package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {
    List<Restaurant> findByRestaurantCuisine(String cuisine);
    List<Restaurant> findByRestaurantName(String name);
    Page<Restaurant> findByRestaurantCuisine(String cuisine, Pageable pageable);

    // 페이징
    Page<Restaurant> findAll(Pageable pageable);
    // 검색결과 페이징
    Page<Restaurant> findAll(Specification<Restaurant> spec, Pageable pageable);

}
