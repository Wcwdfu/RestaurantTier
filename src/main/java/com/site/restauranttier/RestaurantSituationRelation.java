package com.site.restauranttier;

import jakarta.persistence.*;

@Entity
public class RestaurantSituationRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @Id
    private Long categoryId;
    // Getters and Setters...
}
