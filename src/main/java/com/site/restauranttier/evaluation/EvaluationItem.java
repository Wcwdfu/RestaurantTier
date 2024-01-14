package com.site.restauranttier.evaluation;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@Table(name="evaluation_items_tbl")
public class EvaluationItem {
    @Id
    private Integer itemId;
    @OneToMany(mappedBy = "evaluationItem")
    private List<EvaluationItemScore> EvaluationItemScoreList = new ArrayList<>();
    private String itemName;
}
