package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.RestaurantComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantCommentRepository extends JpaRepository<RestaurantComment, Long> {

    @Query("SELECT e, COUNT(a) " +
            "FROM RestaurantComment e " +
            "LEFT JOIN e.restaurantCommentlikeList a " +
            "WHERE e.restaurant = :restaurant " +
            "AND e.status = 'ACTIVE' " +
            "GROUP BY e " +
            "ORDER BY COUNT(a) DESC")
    List<Object[]> findOrderPopular(@Param("restaurant") Restaurant restaurant);

    @Query("SELECT e, COUNT(a) " +
            "FROM RestaurantComment e " +
            "LEFT JOIN e.restaurantCommentlikeList a " +
            "WHERE e.restaurant = :restaurant " +
            "AND e.status = 'ACTIVE' " +
            "GROUP BY e " +
            "ORDER BY e.createdAt DESC")
    List<Object[]> findOrderLatest(@Param("restaurant") Restaurant restaurant);

    Optional<RestaurantComment> findByCommentId(Integer commentId);
}