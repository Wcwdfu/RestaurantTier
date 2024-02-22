package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Evaluation;
import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation,Integer>{
    Optional<Evaluation> findByUserAndRestaurant(User user, Restaurant restaurant);

    Integer countByRestaurant(Restaurant restaurant);

    Integer countAllByStatus(String status);
}
