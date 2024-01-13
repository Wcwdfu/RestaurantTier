package com.site.restauranttier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
public class EvaluationItemScore {
    @Id
    private Long evaluationId;

    @Id
    private Long itemId;

    private Float score;
    // Getters and Setters...
}
