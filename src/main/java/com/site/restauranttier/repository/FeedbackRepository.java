package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Evaluation;
import com.site.restauranttier.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {

}
