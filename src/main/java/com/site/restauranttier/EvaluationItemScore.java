package com.site.restauranttier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="evaluation_item_scores_tbl")
public class EvaluationItemScore {
    @Id
    private int evaluationId;

    @Id
    private int itemId;

    private float score;
    // Getters and Setters...
}
