package com.site.restauranttier;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class RestaurantComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    private String commentBody;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    // Getters and Setters...
}
