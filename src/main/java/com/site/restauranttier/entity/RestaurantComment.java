package com.site.restauranttier.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name="restaurant_comments_tbl")
public class RestaurantComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "restaurantComment")
    private List<RestaurantCommentlike> restaurantCommentlikeList=new ArrayList<>();

    @OneToMany(mappedBy = "restaurantComment")
    private List<RestaurantCommentdislike> restaurantCommentdislikeList=new ArrayList<>();

    private String commentBody;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
