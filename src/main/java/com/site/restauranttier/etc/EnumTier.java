package com.site.restauranttier.etc;

import com.site.restauranttier.dto.RestaurantEverageScoreDTO;
import com.site.restauranttier.entity.Restaurant;
import lombok.Getter;

import java.util.List;

@Getter
public enum EnumTier {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), NONE(-1);

    private final Integer value;

    EnumTier(Integer value) { this.value = value; }

    public static EnumTier calculateTierOfRestaurant(List<RestaurantEverageScoreDTO> restaurantEverageScoreDTOList, Restaurant restaurant) {
        int numberOfData = restaurantEverageScoreDTOList.size();
        int indexOfRestaurant = getIndex(restaurantEverageScoreDTOList, restaurant);

        if (indexOfRestaurant == -1) {
            return EnumTier.NONE;
        }

        // 티어 계산 로직
        double averageScore = restaurantEverageScoreDTOList.get(indexOfRestaurant).getAverageScore();
        if (averageScore >= 6.0) {
            return EnumTier.ONE;
        } else if (averageScore >= 5.0) {
            return EnumTier.TWO;
        } else if (averageScore >= 3.5) {
            return EnumTier.THREE;
        } else if (averageScore >= 2.0) {
            return EnumTier.FOUR;
        } else {
            return EnumTier.FIVE;
        }

        // 티어 계산 로직
        /*double percentOfRestaurant = (indexOfRestaurant + 1.0) / numberOfData;

        if (percentOfRestaurant <= 0.1) {
            return Tier.ONE;
        } else if (percentOfRestaurant <= 0.3) {
            return Tier.TWO;
        } else if (percentOfRestaurant <= 0.7) {
            return Tier.THREE;
        } else if (percentOfRestaurant <= 0.9) {
            return Tier.FOUR;
        } else {
            return Tier.FIVE;
        }*/
    }
    private static int getIndex(List<RestaurantEverageScoreDTO> restaurantEverageScoreDTOList, Restaurant restaurant) {
        for (int i = 0; i < restaurantEverageScoreDTOList.size(); i++) {
            if (restaurantEverageScoreDTOList.get(i).getRestaurant().equals(restaurant)) {
                return i;
            }
        }
        return -1;
    }
}
