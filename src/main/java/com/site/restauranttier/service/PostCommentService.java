package com.site.restauranttier.service;

import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.PostComment;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.PostCommentRepository;
import com.site.restauranttier.repository.PostRepository;
import com.site.restauranttier.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class PostCommentService {
    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void create(Post post, User user, PostComment postComment) {
        PostComment savedPostComment = postCommentRepository.save(postComment);
        user.getPostCommentList().add(savedPostComment);
        post.getPostCommentList().add(savedPostComment);
        userRepository.save(user);
        postRepository.save(post);
    }

    public List<String> getCreatedAtList(List<PostComment> postCommentList) {
        // Comment의 createdAt을 문자열로 변환하여 저장할 리스트
        List<String> commentCreatedAtList = postCommentList.stream()
                .map(comment -> formatDateTime(comment.getCreatedAt()))
                .collect(Collectors.toList());
        return commentCreatedAtList;
    }
    // datetime 타입의 시간을 특정 형식으로 formatting하는 함수
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
