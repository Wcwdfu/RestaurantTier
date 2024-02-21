
package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Situation;
import com.site.restauranttier.etc.EnumSituation;
import com.site.restauranttier.etc.RestaurantTierDataClass;
import com.site.restauranttier.repository.SituationRepository;
import com.site.restauranttier.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final Integer tierPageSize = 40;

    // 티어표 화면
    @GetMapping("/tier")
    public String tier(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "cuisine", required = false, defaultValue = "전체") String cuisine,
            @RequestParam(value = "situation", required = false, defaultValue = "전체") String situation
    ) {
        Pageable pageable = PageRequest.of(page, tierPageSize);
        if (situation.equals("전체") && cuisine.equals("전체")) { // 종류: 전체 & 상황: 전체
            List<RestaurantTierDataClass> restaurantList = evaluationService.getAllRestaurantTierDataClassList();
            model.addAttribute("situation", "전체");
            model.addAttribute("paging", convertToPage(restaurantList, pageable));
        } else if (cuisine.equals("전체")) { // 종류: 전체 & 상황: 전체 아님
            EnumSituation enumSituation = EnumSituation.valueOf(situation);
            Situation situationObject = situationRepository.findBySituationName(enumSituation.getValue());
            List<RestaurantTierDataClass> restaurantList = evaluationService.getRestaurantTierDataClassListBySituation(situationObject);
            model.addAttribute("situation", enumSituation.getValue());
            model.addAttribute("paging", convertToPage(restaurantList, pageable));
        } else if (situation.equals("전체")) { // 종류: 전체 아님 & 상황: 전체
            List<RestaurantTierDataClass> restaurantList = evaluationService.getRestaurantTierDataClassListByCuisine(cuisine);
            model.addAttribute("paging", convertToPage(restaurantList, pageable));
            model.addAttribute("situation", "전체");
        } else { // 종류: 전체 아님 & 상황: 전체 아님
            EnumSituation enumSituation = EnumSituation.valueOf(situation);
            Situation situationObject = situationRepository.findBySituationName(enumSituation.getValue());
            List<RestaurantTierDataClass> restaurantList = evaluationService.getRestaurantTierDataClassListByCuisineAndSituation(cuisine, situationObject);
            model.addAttribute("situation", enumSituation.getValue());
            model.addAttribute("paging", convertToPage(restaurantList, pageable));
        }
        model.addAttribute("situationQueryParameter", situation);
        model.addAttribute("currentPage","tier");
        model.addAttribute("cuisine", cuisine);
        return "tier";
    }

    public Page<RestaurantTierDataClass> convertToPage(List<RestaurantTierDataClass> dataList, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dataList.size());

        return new PageImpl<>(dataList.subList(start, end), pageable, dataList.size());
    }

    // List 전체가 있는 html 코드 반환
    @GetMapping("/api/list/tier")
    public String getTierList(
            Model model,
            @RequestParam(value = "cuisine", required = false, defaultValue = "전체") String cuisine,
            @RequestParam(value = "situation", required = false, defaultValue = "전체") String situation
    ) {
        if (situation.equals("전체") && cuisine.equals("전체")) { // 종류: 전체 & 상황: 전체
            List<RestaurantTierDataClass> restaurantList = evaluationService.getAllRestaurantTierDataClassList();
            model.addAttribute("situation", "전체");
            model.addAttribute("paging", restaurantList);
        } else if (cuisine.equals("전체")) { // 종류: 전체 & 상황: 전체 아님
            EnumSituation enumSituation = EnumSituation.valueOf(situation);
            Situation situationObject = situationRepository.findBySituationName(enumSituation.getValue());
            List<RestaurantTierDataClass> restaurantList = evaluationService.getRestaurantTierDataClassListBySituation(situationObject);
            model.addAttribute("situation", enumSituation.getValue());
            model.addAttribute("paging", restaurantList);
        } else if (situation.equals("전체")) { // 종류: 전체 아님 & 상황: 전체
            List<RestaurantTierDataClass> restaurantList = evaluationService.getRestaurantTierDataClassListByCuisine(cuisine);
            model.addAttribute("paging", restaurantList);
            model.addAttribute("situation", "전체");
        } else { // 종류: 전체 아님 & 상황: 전체 아님
            EnumSituation enumSituation = EnumSituation.valueOf(situation);
            Situation situationObject = situationRepository.findBySituationName(enumSituation.getValue());
            List<RestaurantTierDataClass> restaurantList = evaluationService.getRestaurantTierDataClassListByCuisineAndSituation(cuisine, situationObject);
            model.addAttribute("situation", enumSituation.getValue());
            model.addAttribute("paging", restaurantList);
        }
        model.addAttribute("cuisine", cuisine);
        return "tierTable";
    }

    @GetMapping("/api/page/tier")
    public String getPageList(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "cuisine", required = false, defaultValue = "전체") String cuisine,
            @RequestParam(value = "situation", required = false, defaultValue = "전체") String situation
    ) {
        Pageable pageable = PageRequest.of(page, tierPageSize);
        if (situation.equals("전체") && cuisine.equals("전체")) { // 종류: 전체 & 상황: 전체
            List<RestaurantTierDataClass> restaurantList = evaluationService.getAllRestaurantTierDataClassList();
            model.addAttribute("situation", "전체");
            model.addAttribute("paging", convertToPage(restaurantList, pageable));
        } else if (cuisine.equals("전체")) { // 종류: 전체 & 상황: 전체 아님
            EnumSituation enumSituation = EnumSituation.valueOf(situation);
            Situation situationObject = situationRepository.findBySituationName(enumSituation.getValue());
            List<RestaurantTierDataClass> restaurantList = evaluationService.getRestaurantTierDataClassListBySituation(situationObject);
            model.addAttribute("situation", enumSituation.getValue());
            model.addAttribute("paging", convertToPage(restaurantList, pageable));
        } else if (situation.equals("전체")) { // 종류: 전체 아님 & 상황: 전체
            List<RestaurantTierDataClass> restaurantList = evaluationService.getRestaurantTierDataClassListByCuisine(cuisine);
            model.addAttribute("paging", convertToPage(restaurantList, pageable));
            model.addAttribute("situation", "전체");
        } else { // 종류: 전체 아님 & 상황: 전체 아님
            EnumSituation enumSituation = EnumSituation.valueOf(situation);
            Situation situationObject = situationRepository.findBySituationName(enumSituation.getValue());
            List<RestaurantTierDataClass> restaurantList = evaluationService.getRestaurantTierDataClassListByCuisineAndSituation(cuisine, situationObject);
            model.addAttribute("situation", enumSituation.getValue());
            model.addAttribute("paging", convertToPage(restaurantList, pageable));
        }
        model.addAttribute("cuisine", cuisine);
        return "tierTable";
    }
}
