package com.site.restauranttier.etc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.RestaurantSituationRelation;
import com.site.restauranttier.entity.Situation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RestaurantTierDataClass {
    @JsonIgnore
    private String ranking;
    private Restaurant restaurant;
    @JsonIgnore
    private Integer situationTier;
    private Boolean isFavorite = false;
    private Boolean isEvaluation = false;
    @JsonIgnore
    private List<RestaurantSituationRelation> restaurantSituationRelationList = new ArrayList<>();

    public RestaurantTierDataClass(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public RestaurantTierDataClass (Restaurant restaurant, Integer situationTier) {
        this.restaurant = restaurant;
        this.situationTier = situationTier;
    }

    public void addSituation(RestaurantSituationRelation restaurantSituationRelation) {
        this.restaurantSituationRelationList.add(restaurantSituationRelation);
    }
}
