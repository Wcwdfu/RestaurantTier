package com.site.restauranttier;

import com.site.restauranttier.evaluation.Evaluation;
import com.site.restauranttier.restaurant.RestaurantComment;
import com.site.restauranttier.restaurant.RestaurantCommentlike;
import com.site.restauranttier.restaurant.RestaurantFavorite;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users_tbl")
public class User {
    @Id
    private String userId;

    @OneToMany(mappedBy = "user")
    private List<RestaurantComment> restaruantCommentList = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Evaluation> EvaluationList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<RestaurantFavorite> restaurantFavoriteList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<RestaurantCommentlike> restaurantCommentlikeList = new ArrayList<>();

    private String userPassword;
    private String userEmail;
    private String userNickname;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

