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

@Service
@RequiredArgsConstructor
public class PostCommentService {
    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public void create(Post post, User user, PostComment postComment){
        PostComment savedPostComment = postCommentRepository.save(postComment);
        user.getPostCommentList().add(savedPostComment);
        post.getPostCommentList().add(savedPostComment);
        userRepository.save(user);
        postRepository.save(post);
    }
}
