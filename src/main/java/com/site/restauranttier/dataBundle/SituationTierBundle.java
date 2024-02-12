package com.site.restauranttier.dataBundle;

import com.site.restauranttier.etc.EnumSituation;
import com.site.restauranttier.etc.EnumTier;

public class SituationTierBundle {
    private EnumSituation situation;
    private EnumTier tier;

    public SituationTierBundle(EnumSituation situation, EnumTier tier) {
        this.situation = situation;
        this.tier = tier;
    }
}
