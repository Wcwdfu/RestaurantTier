package com.site.restauranttier;

import jakarta.persistence.*;

@Entity
@IdClass(EvaluationItemScoreId.class)
@Table(name="evaluation_item_scores_tbl")
public class EvaluationItemScore {

    @Id
    @ManyToOne
    @JoinColumn(name="evaluation_id")
    private Evaluation evaluation;
    @Id
    @ManyToOne
    @JoinColumn(name="item_id")
    private EvaluationItem evaluationItem;

    private float score;
}
