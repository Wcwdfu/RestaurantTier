package com.site.restauranttier.repository;

import com.site.restauranttier.entity.RestaurantComment;
import com.site.restauranttier.entity.RestaurantCommentlike;
import com.site.restauranttier.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantCommentLikeRepository extends JpaRepository<RestaurantCommentlike, Integer> {
    Optional<RestaurantCommentlike> findByUserAndRestaurantComment(User user, RestaurantComment restaurantComment);
}
