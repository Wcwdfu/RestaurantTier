package com.site.restauranttier;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="restaurant_hashtags_tbl")
public class RestaurantHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hashtagId;

    private String hashtagName;

    @ManyToMany(mappedBy = "restaurantHashtagList")
    private List<Restaurant> restaurantList = new ArrayList<>();

}
