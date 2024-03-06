package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.Situation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SituationRepository extends JpaRepository<Situation,Integer> {
    Situation findBySituationName(String situationName);
}
