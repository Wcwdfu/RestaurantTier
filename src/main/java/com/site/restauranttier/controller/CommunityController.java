package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.PostComment;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.PostCommentRepository;
import com.site.restauranttier.repository.PostRepository;
import com.site.restauranttier.repository.UserRepository;
import com.site.restauranttier.service.PostCommentService;
import com.site.restauranttier.service.PostService;
import com.site.restauranttier.service.UserService;
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
    private final PostService postService;
    private final UserService userService;
    private final PostCommentService postCommentService;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    // 커뮤니티 메인화면
    @GetMapping("/community")
    public String community(Model model) {
        List<Post> postList = postService.getList();
        LocalDateTime now = LocalDateTime.now();

        // postList의 createdAt 필드를 문자열 형식으로 만들어 timeAgoList에 할당
        List<String> timeAgoList = postList.stream()
                .map(post -> {
                    LocalDateTime createdAt = post.getCreatedAt();
                    // datetime 타입의 createdAt을 string 타입으로 변환해주는 함수
                    return timeAgo(now, createdAt);
                })
                .collect(Collectors.toList());
        // postList와 각각의 createdAt을 문자열로 변환한 List를 모델에 주입한다.
        model.addAttribute("postList", postList);
        model.addAttribute("timeAgoList", timeAgoList);
        return "community";
    }

    // 커뮤니티 게시글 화면
    @GetMapping("/community/{postId}")
    public String post(Model model, @PathVariable Integer postId) {
        Post post = postService.getPost(postId);
        String timeAgoData = timeAgo(LocalDateTime.now(), post.getCreatedAt());
        List<PostComment> postCommentList = post.getPostCommentList();

        // Comment의 createdAt을 문자열로 변환하여 저장할 리스트
        List<String> commentCreatedAtList = postCommentList.stream()
                .map(comment -> formatDateTime(comment.getCreatedAt()))
                .collect(Collectors.toList());

        model.addAttribute("post", post);
        model.addAttribute("commentCreatedAtList", commentCreatedAtList);
        model.addAttribute("timeAgoData", timeAgoData);
        return "community_post";
    }

    // 커뮤니티 글 작성화면
    @GetMapping("/community/write")
    public String write() {
        return "community_write";
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
        User user = userService.getUser(principal.getName());
        postService.create(post, user);
        return ResponseEntity.ok("글이 성공적으로 저장되었습니다.");
    }

    // 댓글 생성
    @PostMapping("/api/community/comment/create")
    public ResponseEntity<String> postCommentCreate(
            @RequestParam("content") String content,
            @RequestParam("postId") String postId,
            Model model, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        Integer postidInt = Integer.valueOf(postId);
        User user = userService.getUser(principal.getName());
        Post post = postService.getPost(postidInt);
        PostComment postComment = new PostComment(content, "ACTIVE", LocalDateTime.now(), post, user);
        postCommentService.create(post, user, postComment);
        return ResponseEntity.ok("댓글이 성공적으로 저장되었습니다.");
    }


    // 작성글이나 댓글이 만들어진지 얼마나 됐는지 계산하는 함수
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


    // datetime 타입의 시간을 특정 형식으로 formatting하는 함수
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
