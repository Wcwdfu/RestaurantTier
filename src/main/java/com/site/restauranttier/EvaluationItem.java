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
@Table(name = "evaluation_item_tbl")
public class EvaluationItem {

    @Id
    @Column(name = "item_id")
    private Long evaluationId;



    @Column(name = "item_name")
    private Integer score;


}