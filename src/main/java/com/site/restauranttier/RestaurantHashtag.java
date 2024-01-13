package com.site.restauranttier;

import jakarta.persistence.*;

@Entity
public class RestaurantHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hashtagId;

    private String hashtagName;
    // Getters and Setters...
}
