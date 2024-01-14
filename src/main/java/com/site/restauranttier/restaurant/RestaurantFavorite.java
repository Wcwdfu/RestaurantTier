package com.site.restauranttier.restaurant;

import com.site.restauranttier.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="restaurant_favorite_tbl")
public class RestaurantFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer favoriteId;
    @ManyToOne
    @JoinColumn(name="user_id")
    User user;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    Restaurant restaurant;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
