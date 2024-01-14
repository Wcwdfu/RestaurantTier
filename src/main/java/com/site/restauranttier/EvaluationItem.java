package com.site.restauranttier;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="evaluation_items_tbl")
public class EvaluationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;
    @OneToMany(mappedBy = "evaluationItem")
    private List<EvaluationItemScore> EvaluationItemScoreList = new ArrayList<>();
    private String itemName;
}
