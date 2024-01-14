package com.site.restauranttier;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="situation_categories")
public class SituationCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @ManyToMany(mappedBy = "SituationCategoryList")
    private List<Restaurant> restaurantList = new ArrayList<>();
    private String categoryName;
}
