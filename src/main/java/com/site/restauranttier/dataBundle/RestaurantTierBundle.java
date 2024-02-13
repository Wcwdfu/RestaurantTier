package com.site.restauranttier.dataBundle;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.etc.EnumSituation;
import com.site.restauranttier.etc.EnumTier;
import lombok.Getter;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RestaurantTierBundle {
    private Restaurant restaurant;
    private EnumTier tier;

    public RestaurantTierBundle(Restaurant restaurant, EnumTier tier) {
        this.restaurant = restaurant;
        this.tier = tier;
    }
}
