package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@Setter
@Table(name="situations_TBL")
public class Situation {
    @Id
    private Integer situationId;

    private String situationName;


    public Situation(Integer id, String situationName) {
        this.situationId=id;
        this.situationName = situationName;
    }
    public Situation(){

    };
    @ManyToMany(mappedBy = "situationList")
    private List<Restaurant> restaurantList = new ArrayList<>();

    @OneToMany(mappedBy = "situation")
    private List<EvaluationItemScore> evaluationItemScoreList = new ArrayList<>();
}
