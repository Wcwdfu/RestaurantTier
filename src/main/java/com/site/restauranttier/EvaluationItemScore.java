package com.site.restauranttier;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "evaluation_item_scores_tbl")
public class EvaluationItemScore {

    @Id
    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "score")
    private Integer score;


}
