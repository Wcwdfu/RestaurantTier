package com.site.restauranttier.etc;

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
    private Restaurant restaurant;
    private Integer situationTier;
    private List<Situation> situationList = new ArrayList<>();

    public RestaurantTierDataClass(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public RestaurantTierDataClass (Restaurant restaurant, Integer situationTier) {
        this.restaurant = restaurant;
        this.situationTier = situationTier;
    }

    public void addSituation(Situation situation) {
        this.situationList.add(situation);
    }
}
