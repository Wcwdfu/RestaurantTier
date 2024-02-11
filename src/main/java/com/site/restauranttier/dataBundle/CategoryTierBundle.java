package com.site.restauranttier.dataBundle;

import com.site.restauranttier.etc.EnumTier;
import lombok.Getter;

@Getter
public class CategoryTierBundle {
    private String tierCategoryName;
    private EnumTier tier;

    public CategoryTierBundle(String tierCategoryName, EnumTier tier) {
        this.tierCategoryName = tierCategoryName;
        this.tier = tier;
    }
}
