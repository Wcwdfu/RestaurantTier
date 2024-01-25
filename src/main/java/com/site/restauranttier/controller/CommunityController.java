package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Post;
import com.site.restauranttier.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommunityController {
    private final PostRepository postRepository;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/community")
    public String community(Model model) {
        List<Post> postList = postRepository.findAll();
        model.addAttribute("postList",postList);
        return "community";
    }



}
