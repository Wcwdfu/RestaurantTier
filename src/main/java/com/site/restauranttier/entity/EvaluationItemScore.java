package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
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
    @JoinColumn(name="situation_id")
    private Situation situation;

    private double score;


}
