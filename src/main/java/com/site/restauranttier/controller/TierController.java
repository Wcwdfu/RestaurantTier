package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class TierController {
    private final RestaurantRepository restaurantRepository;

    // 티어표 화면
    @GetMapping("/tier")
    public String tier(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "cuisine", required = false) String cuisine
    ) {
        // 메인에서 이미지로 티어표로 이동할 때
        Pageable pageable = PageRequest.of(page, 30);
        if (cuisine != null && !cuisine.isEmpty() && !"null".equals(cuisine)) {
            Page<Restaurant> paging = restaurantRepository.findByRestaurantCuisineAndStatus(cuisine, "ACTIVE", pageable);
            model.addAttribute("paging", paging);
            model.addAttribute("cuisine", cuisine);
            return "tier";
        } else {
            // 상단 탭을 통해 티어표로 이동할 때
            Page<Restaurant> paging = this.restaurantRepository.findByStatus("ACTIVE", pageable);
            model.addAttribute("paging", paging);
            return "tier";
        }
    }


}
