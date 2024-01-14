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
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    private String commentBody;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
