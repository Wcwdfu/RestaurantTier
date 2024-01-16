package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "restaurant_comment_likes_tbl")
public class RestaurantCommentlike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="comment_id")
    private RestaurantComment restaurantComment;



    private LocalDateTime createdAt;
}
