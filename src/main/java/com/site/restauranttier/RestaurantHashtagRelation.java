package com.site.restauranttier;

import jakarta.persistence.*;

@Entity
public class RestaurantHashtagRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @Id
    private Long hashtagId;
    // Getters and Setters...
}
