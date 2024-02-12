package com.site.restauranttier.dataBundle;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.etc.EnumSituation;
import com.site.restauranttier.etc.EnumTier;
import lombok.Getter;

@Getter
public class  RestaurantAverageScoreBundle {
    private Restaurant restaurant;
    private Double averageScore;

    public RestaurantAverageScoreBundle(Restaurant restaurant, Double averageScore) {
        this.restaurant = restaurant;
        this.averageScore = averageScore;
    }
}
