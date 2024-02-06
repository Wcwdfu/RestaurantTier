package com.site.restauranttier.dto;

import com.site.restauranttier.entity.Restaurant;
import lombok.Getter;

@Getter
public class RestaurantEverageScoreDTO {
    private Restaurant restaurant;
    private Double averageScore;

    public RestaurantEverageScoreDTO(Restaurant restaurant, Double averageScore) {
        this.restaurant = restaurant;
        this.averageScore = averageScore;
    }
}
