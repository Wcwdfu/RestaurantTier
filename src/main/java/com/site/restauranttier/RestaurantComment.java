package com.site.restauranttier;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="restaurant_comments_tbl")
public class RestaurantComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    @ManyToOne
    @JoinColumn(name="restaurantId")
    private Restaurant restaurant;

    private String commentBody;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Getters and Setters...
}
