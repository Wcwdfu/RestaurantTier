package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {
    List<Restaurant> findByRestaurantCuisine(String cuisine);
    List<Restaurant> findByRestaurantName(String name);
}
