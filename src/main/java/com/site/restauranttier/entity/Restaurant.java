package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@Table(name = "restaurants_tbl")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restaurantId;


    private String restaurantName;
    private String restaurantType;
    private String restaurantPosition;
    private String restaurantAddress;
    private String restaurantTel;
    @Column(unique = true)
    private String restaurantUrl;
    private String restaurantCuisine;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer restaurantVisitCount;

    // 다른 테이블과의 관계 매핑
    @ManyToMany
    @JoinTable(name = "restaurant_hashtag_relations_tbl", joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name="hashtag_id"))
    List<RestaurantHashtag> restaurantHashtagList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "restaurant_situation_relations_tbl", joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name="category_id"))
    List<SituationCategory> SituationCategoryList = new ArrayList<>();
    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantComment> restaurantCommentList = new ArrayList<>();;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantFavorite> restaurantFavorite = new ArrayList<>();;
}
