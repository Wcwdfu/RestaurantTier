package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@Table(name="situations_tbl")
public class Situation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer situationId;

    private String situationName;


    public Situation(String situationName) {
        this.situationName = situationName;
    }
    public Situation(){

    };
    @ManyToMany(mappedBy = "situationList")
    private List<Restaurant> restaurantList = new ArrayList<>();

    @OneToMany(mappedBy = "situation")
    private List<EvaluationItemScore> evaluationItemScoreList = new ArrayList<>();
}
