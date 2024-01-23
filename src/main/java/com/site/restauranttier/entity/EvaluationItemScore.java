package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@IdClass(EvaluationItemScoreId.class)
@Table(name="evaluation_item_scores_TBL")
public class EvaluationItemScore {

    @Id
    @ManyToOne
    @JoinColumn(name="evaluation_id")
    private Evaluation evaluation;
    @Id
    @ManyToOne
    @JoinColumn(name="situation_id")
    private Situation situation;

    public EvaluationItemScore(Evaluation evaluation, Situation situation, Double score) {
        this.evaluation = evaluation;
        this.situation = situation;
        this.score = score;
    }

    private Double score;


    public EvaluationItemScore() {

    }
}
