package com.site.restauranttier.repository;

import com.site.restauranttier.entity.EvaluationItemScore;
import com.site.restauranttier.entity.EvaluationItemScoreId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationItemScoreRepository extends JpaRepository<EvaluationItemScore, EvaluationItemScoreId> {
}
