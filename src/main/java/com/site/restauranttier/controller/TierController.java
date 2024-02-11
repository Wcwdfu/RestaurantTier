package com.site.restauranttier.controller;

import com.site.restauranttier.dataBundle.RestaurantTierBundle;
import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.etc.EnumSituation;
import com.site.restauranttier.repository.RestaurantRepository;
import com.site.restauranttier.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class TierController {
    private final RestaurantRepository restaurantRepository;
    private final EvaluationService evaluationService;

    // 티어표 화면
    @GetMapping("/tier")
    public String tier(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "cuisine", required = false, defaultValue = "전체") String cuisine,
            @RequestParam(value = "situation", required = false, defaultValue = "전체") String situation
    ) {
        Pageable pageable = PageRequest.of(page, 100);
        if (situation.equals("전체") && cuisine.equals("전체")) {
            List<RestaurantTierBundle> restaurantTierBundleList = evaluationService.getAllRestaurantTierBundleList();
            model.addAttribute("situation", "전체");
            model.addAttribute("restaurantTierBundleList", restaurantTierBundleList);
        } else if (cuisine.equals("전체")) {
            EnumSituation enumSituation = EnumSituation.valueOf(situation);
            /*List<Restaurant> paging = this.restaurantRepository.findActiveRestaurantsBySituation(enumSituation.getValue(), "ACTIVE");
            model.addAttribute("situation", enumSituation.getValue());
            model.addAttribute("paging", paging);*/
        } else if (situation.equals("전체")) {
            List<RestaurantTierBundle> restaurantTierBundleList = evaluationService.getCuisineRestaurantTierBundleList(cuisine);
            model.addAttribute("restaurantTierBundleList", restaurantTierBundleList);
            model.addAttribute("situation", "전체");
        } else {
            EnumSituation enumSituation = EnumSituation.valueOf(situation);
            List<Restaurant> paging = this.restaurantRepository.findActiveRestaurantsByCuisineAndSituation(cuisine, enumSituation.getValue(), "ACTIVE");
            model.addAttribute("situation", enumSituation.getValue());
            model.addAttribute("paging", paging);
        }
        model.addAttribute("cuisine", cuisine);
        return "tier";
    }
}
