package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.PostComment;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.PostCommentRepository;
import com.site.restauranttier.repository.PostRepository;
import com.site.restauranttier.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CommunityController {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;
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
    public String post(Model model, @PathVariable Integer postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return "community_post";
        }
        Post post = postOptional.get();
        String timeAgoData = timeAgo(LocalDateTime.now(), post.getCreatedAt());
        List<PostComment> postCommentList = post.getPostCommentList();

        // Comment의 createdAt을 문자열로 변환하여 저장할 리스트
        List<String> commentCreatedList = postCommentList.stream()
                .map(comment -> formatDateTime(comment.getCreatedAt()))
                .collect(Collectors.toList());
        model.addAttribute("commentCreatedList",commentCreatedList);

        model.addAttribute("post", post);
        model.addAttribute("timeAgoData", timeAgoData);
        return "community_post";
    }

    @GetMapping("/community/write")
    public String write() {
        return "community_write";
    }

    // 만들어 진지 얼마나 됐는지 계산하는 함수
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
    // 날짜, 시간 생성
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    // 게시글 생성
    @PostMapping("/api/community/post/create")
    public ResponseEntity<String> postCreate(
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("content") String content,
            Model model, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        Post post = new Post(title, content, category, "ACTIVE", LocalDateTime.now());
        Optional<User> userOptional = userRepository.findByUserTokenId(principal.getName());
        User user = userOptional.get();
        post.setUser(user);
        Post savedpost = postRepository.save(post);
        user.getPostList().add(savedpost);
        userRepository.save(user);
        return ResponseEntity.ok("글이 성공적으로 저장되었습니다.");
    }
    // 댓글 생성
    @PostMapping("/api/community/comment/create")
    public ResponseEntity<String> postCreate(
            @RequestParam("content") String content,
            @RequestParam("postId") String postId,
            Model model, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        Integer postidInt= Integer.valueOf(postId);
        Optional<User> userOptional = userRepository.findByUserTokenId(principal.getName());
        User user = userOptional.get();
        Optional<Post> postOptional = postRepository.findById(postidInt);
        Post post= postOptional.get();

        PostComment postComment = new PostComment(content,"ACTIVE",LocalDateTime.now(),post,user);
        PostComment savedPostComment = postCommentRepository.save(postComment);
        user.getPostCommentList().add(savedPostComment);
        post.getPostCommentList().add(savedPostComment);
        userRepository.save(user);
        postRepository.save(post);

        return ResponseEntity.ok("댓글이 성공적으로 저장되었습니다.");
    }
}
