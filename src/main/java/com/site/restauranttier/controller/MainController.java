package com.site.restauranttier.controller;

import com.site.restauranttier.entity.*;
import com.site.restauranttier.etc.JsonData;
import com.site.restauranttier.repository.EvaluationRepository;
import com.site.restauranttier.repository.RestaurantRepository;
import com.site.restauranttier.repository.SituationRepository;
import com.site.restauranttier.repository.UserRepository;
import com.site.restauranttier.service.RestaurantCommentService;
import com.site.restauranttier.service.RestaurantService;
import groovy.util.Eval;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Controller
public class MainController {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    //@Value("#{'${restaurant.cuisines}'.split(',\\s*')}")
    private List<String> cuisines = Arrays.asList(
            "전체", "한식", "일식", "중식", "양식", "아시안", "고기",
            "치킨", "해산물", "햄버거/피자", "분식", "술집", "카페/디저트",
            "베이커리", "기타"
    );

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }
    // 홈 화면
    @GetMapping("/home")
    public String home(
            Model model
    ) {
        // 랜덤 뽑기 관련 분류 데이터
        model.addAttribute("cuisines", cuisines);
        return "home";
    }

    // 랭킹 화면
    @GetMapping("/ranking")
    public String ranking() {
        return "ranking";
    }

    // 추천 화면
    @GetMapping("/recommend")
    public String recommend() {
        return "recommend";
    }


    // 티어표 화면
    @GetMapping("/tier")
    public String tier(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "cuisine", required = false) String cuisine) {
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
            logger.info(paging.toString());
            model.addAttribute("paging", paging);
            return "tier";
        }

    }


    // 검색 결과 화면
    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Restaurant> paging = this.restaurantService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "searchResult";
    }
}




