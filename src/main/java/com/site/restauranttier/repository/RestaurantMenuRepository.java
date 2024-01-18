package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.RestaurantMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Integer> {
    List<RestaurantMenu> findByRestaurantOrderByMenuId(Restaurant restaurant);
}