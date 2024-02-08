package com.site.restauranttier.dto;

import com.site.restauranttier.etc.EnumTier;
import lombok.Getter;

@Getter
public class RestaurantTierDTO {
    private String tierCategoryName;
    private EnumTier tier;

    public RestaurantTierDTO(String tierCategoryName, EnumTier tier) {
        this.tierCategoryName = tierCategoryName;
        this.tier = tier;
    }
}
