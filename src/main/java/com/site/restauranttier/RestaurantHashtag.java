package com.site.restauranttier;

import jakarta.persistence.*;

@Entity
@Table(name="restaurant_hashtags_tbl")
public class RestaurantHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hashtagId;

    private String hashtagName;
}
