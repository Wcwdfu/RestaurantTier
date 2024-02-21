package com.site.restauranttier.etc;

import lombok.Getter;

import java.util.List;

@Getter
public enum EnumTier {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), NONE(-1);

    private final Integer value;

    EnumTier(Integer value) { this.value = value; }

    public static EnumTier calculateTierOfRestaurant(Double averageScore) {
        if (averageScore >= 6.3) {
            return EnumTier.ONE;
        } else if (averageScore >= 5.5) {
            return EnumTier.TWO;
        } else if (averageScore >= 3.7) {
            return EnumTier.THREE;
        } else if (averageScore >= 2.3) {
            return EnumTier.FOUR;
        } else if (averageScore >= 1.7) {
            return EnumTier.FIVE;
        } else {
            return EnumTier.NONE;
        }
    }

    public static EnumTier calculateSituationTierOfRestaurant(Double averageScore) {
        if (averageScore >= 4.3) {
            return EnumTier.ONE;
        } else if (averageScore >= 3.9) {
            return EnumTier.TWO;
        } else if (averageScore >= 3.5) {
            return EnumTier.THREE;
        } else if (averageScore >= 3.0) {
            return EnumTier.FOUR;
        } else if (averageScore >= 2.0) {
            return EnumTier.FIVE;
        } else {
            return EnumTier.NONE;
        }
    }

    public static EnumTier fromValue(Integer tier) {
        for (EnumTier myEnum : EnumTier.values()) {
            if (myEnum.value.equals(tier)) {
                return myEnum;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + tier);
    }
}
