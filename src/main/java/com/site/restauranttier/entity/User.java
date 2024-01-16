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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(unique = true,nullable = false)
    private String userTokenId;

    private String userPassword;
    @Column(unique = true, nullable = false)
    private String userEmail;
    @Column(unique = true, nullable = false)

    private String userNickname;
    @Column(nullable = false)

    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    @Column(nullable = false)
    private String loginApi;

    @OneToMany(mappedBy = "user")
    private List<RestaurantComment> restaruantCommentList = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Evaluation> evaluationList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<RestaurantFavorite> restaurantFavoriteList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<RestaurantCommentlike> restaurantCommentlikeList = new ArrayList<>();

    public User(String userTokenId, String userPassword, String userEmail, String userNickname, String status, LocalDateTime createdAt) {
        this.userTokenId = userTokenId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.status = status;
        this.createdAt = createdAt;
    }

    public User() {

    }
}

