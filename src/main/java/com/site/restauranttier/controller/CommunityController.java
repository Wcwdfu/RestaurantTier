package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Post;
import com.site.restauranttier.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CommunityController {
    private final PostRepository postRepository;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/community")
    public String community(Model model) {
        List<Post> postList = postRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        List<String> timeAgoList = postList.stream()
                .map(post -> {
                    LocalDateTime createdAt = post.getCreatedAt();
                    return timeAgo(now, createdAt);
                })
                .collect(Collectors.toList());

        model.addAttribute("postList", postList);
        model.addAttribute("timeAgoList", timeAgoList);
        return "community";
    }

     @GetMapping("/community/{postId}")
    public String post(Model model, @PathVariable Integer postId){
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isEmpty()){
            return "community_post";
        }
        Post post=postOptional.get();
         String timeAgoData= timeAgo(LocalDateTime.now(), post.getCreatedAt());

         model.addAttribute("post",post);
        model.addAttribute("timeAgoData",timeAgoData);
        return "community_post";
     }
     @GetMapping("/community/write")
     public String write(){
        return "community_write";
     }
     
     // 만들어 진지 얼마나 됐는지 계싼하는 함수
    private String timeAgo(LocalDateTime now, LocalDateTime past) {
        long minutes = Duration.between(past, now).toMinutes();
        if (minutes < 60) {
            return minutes + "분 전";
        }
        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "시간 전";
        }
        long days = hours / 24;
        return days + "일 전";
    }

}
