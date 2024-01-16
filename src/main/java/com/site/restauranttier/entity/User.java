package com.site.restauranttier.entity;

import com.site.restauranttier.entity.Evaluation;
import com.site.restauranttier.entity.RestaurantComment;
import com.site.restauranttier.entity.RestaurantCommentlike;
import com.site.restauranttier.entity.RestaurantFavorite;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    public User(String userId, String userPassword, String userEmail, String userNickname, String status, LocalDateTime createdAt) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.status = status;
        this.createdAt = createdAt;
    }
    public User(){

    }

    private String userPassword;
    private String userEmail;

    private String userNickname;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

