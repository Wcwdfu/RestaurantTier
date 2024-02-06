package com.site.restauranttier.dto;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.etc.Tier;
import lombok.Getter;

@Getter
public class RestaurantTierDTO {
    private String tierCategoryName;
    private Tier tier;

    public RestaurantTierDTO(String tierCategoryName, Tier tier) {
        this.tierCategoryName = tierCategoryName;
        this.tier = tier;
    }
}
