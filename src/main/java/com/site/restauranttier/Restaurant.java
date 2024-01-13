package com.site.restauranttier;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    private String restaurantName;
    private String restaurantType;
    private String restaurantPosition;
    private String restaurantAddress;
    private String restaurantTel;
    private String restaurantUrl;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    // Getters and Setters...
}
