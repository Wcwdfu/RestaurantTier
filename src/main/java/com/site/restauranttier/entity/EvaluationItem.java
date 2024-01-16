package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@Table(name="evaluation_items_tbl")
public class EvaluationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @Column(unique = true)
    private String itemName;

    @OneToMany(mappedBy = "evaluationItem")
    private List<EvaluationItemScore> EvaluationItemScoreList = new ArrayList<>();


}
