
package com.site.restauranttier.controller;

import com.site.restauranttier.dataBundle.RestaurantTierBundle;
import com.site.restauranttier.entity.Situation;
import com.site.restauranttier.etc.EnumSituation;
import com.site.restauranttier.repository.SituationRepository;
import com.site.restauranttier.service.EvaluationService;
import lombok.RequiredArgsConstructor;
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
    private final SituationRepository situationRepository;
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
            Situation situationObject = situationRepository.findBySituationName(enumSituation.getValue());
            List<RestaurantTierBundle> restaurantTierBundleList = evaluationService.getRestaurantTierBundleListBySituation(situationObject);
            model.addAttribute("situation", enumSituation.getValue());
            model.addAttribute("restaurantTierBundleList", restaurantTierBundleList);
        } else if (situation.equals("전체")) {
            List<RestaurantTierBundle> restaurantTierBundleList = evaluationService.getRestaurantTierBundleListByCuisine(cuisine);
            model.addAttribute("restaurantTierBundleList", restaurantTierBundleList);
            model.addAttribute("situation", "전체");
        } else {
            EnumSituation enumSituation = EnumSituation.valueOf(situation);
            Situation situationObject = situationRepository.findBySituationName(enumSituation.getValue());
            List<RestaurantTierBundle> restaurantTierBundleList = evaluationService.getRestaurantTierBundleListByCuisineAndSituation(cuisine, situationObject);
            model.addAttribute("situation", enumSituation.getValue());
            model.addAttribute("restaurantTierBundleList", restaurantTierBundleList);
        }
        model.addAttribute("currentPage","tier");
        model.addAttribute("cuisine", cuisine);
        return "tier";
    }
}
