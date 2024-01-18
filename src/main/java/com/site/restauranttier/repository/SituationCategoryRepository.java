package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Situation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SituationCategoryRepository extends JpaRepository<Situation,Integer> {
}
