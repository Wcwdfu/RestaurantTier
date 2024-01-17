package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@Table(name="situation_categories_tbl")
public class SituationCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    public SituationCategory(String categoryName) {
        this.categoryName = categoryName;
    }
    public SituationCategory(){

    };
    @ManyToMany(mappedBy = "SituationCategoryList")
    private List<Restaurant> restaurantList = new ArrayList<>();
    private String categoryName;
}
