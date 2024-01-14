package com.site.restauranttier.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {
    List<Restaurant> findByRestaurantCuisine(String cuisine);
}
