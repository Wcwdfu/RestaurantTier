package com.site.restauranttier;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="restaurants_tbl")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int restaurantId;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantComment> restaurantCommentList;
    private String restaurantName;
    private String restaurantType;
    private String restaurantPosition;
    private String restaurantAddress;
    private String restaurantTel;
    private String restaurantUrl;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Getters and Setters...
}
