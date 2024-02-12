package com.site.restauranttier.dataBundle;

import com.site.restauranttier.entity.Restaurant;
import lombok.Getter;

@Getter
public class RestaurantAverageScoreBundle {
    private Restaurant restaurant;
    private Double averageScore;

    public RestaurantAverageScoreBundle(Restaurant restaurant, Double averageScore) {
        this.restaurant = restaurant;
        this.averageScore = averageScore;
    }
    public RestaurantAverageScoreBundle(Restaurant restaurant, Integer averageScore) {
        this.restaurant = restaurant;
        this.averageScore = averageScore + 0.0;
    }
}
