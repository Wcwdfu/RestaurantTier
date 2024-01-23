package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@Table(name="restaurant_hashtags_TBL")
public class RestaurantHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hashtagId;

    private String hashtagName;

    @ManyToMany(mappedBy = "restaurantHashtagList")
    private List<Restaurant> restaurantList = new ArrayList<>();

}
