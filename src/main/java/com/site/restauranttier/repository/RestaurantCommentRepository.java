package com.site.restauranttier.repository;

import com.site.restauranttier.entity.RestaurantComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantCommentRepository extends JpaRepository<RestaurantComment,Integer> {
}
