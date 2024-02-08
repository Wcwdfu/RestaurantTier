package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.RestaurantFavorite;
import com.site.restauranttier.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantFavoriteRepository extends JpaRepository<RestaurantFavorite, Integer> {
    Optional<RestaurantFavorite> findByUserAndRestaurant(User user, Restaurant restaurant);

    Integer countByRestaurantAndStatus(Restaurant restaurant, String status);
}
