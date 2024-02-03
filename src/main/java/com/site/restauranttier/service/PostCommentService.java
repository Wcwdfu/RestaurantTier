package com.site.restauranttier.service;

import com.site.restauranttier.DataNotFoundException;
import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.PostComment;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.PostCommentRepository;
import com.site.restauranttier.repository.PostRepository;
import com.site.restauranttier.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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

    public PostComment getPostCommentByCommentId(Integer commentId) {
        Optional<PostComment> postComment = postCommentRepository.findById(commentId);
        if (postComment.isPresent()) {
            return postComment.get();
        } else {
            throw new DataNotFoundException("PostComment not found");
        }
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

    public void likeCreateOrDelete(PostComment postcomment, User user) {
        List<User> likeUserList = postcomment.getLikeUserList();
        List<User> dislikeUserList = postcomment.getDislikeUserList();
        List<PostComment> likePostCommentList = user.getLikePostCommentList();
        List<PostComment> dislikePostCommentList = user.getDislikePostCommentList();
        //해당 postcomment를 like 한 경우 - 제거
        if (likeUserList.contains(user)) {
            postcomment.setLikeCount(postcomment.getLikeCount() - 1);

            likePostCommentList.remove(postcomment);
            likeUserList.remove(user);
        }
        //해당 postcomment를 이미 dislike 한 경우 - 제거하고 추가
        else if (dislikeUserList.contains(user)) {
            postcomment.setLikeCount(postcomment.getLikeCount() + 2);

            dislikeUserList.remove(user);
            dislikePostCommentList.remove(postcomment);
            likeUserList.add(user);
            likePostCommentList.add(postcomment);
        }
        // 처음 dislike 하는 경우-추가
        else {
            postcomment.setLikeCount(postcomment.getLikeCount() + 1);

            likeUserList.add(user);
            likePostCommentList.add(postcomment);
        }
        postCommentRepository.save(postcomment);
        userRepository.save(user);
    }

    public void dislikeCreateOrDelete(PostComment postcomment, User user) {
        List<User> likeUserList = postcomment.getLikeUserList();
        List<User> dislikeUserList = postcomment.getDislikeUserList();
        List<PostComment> likePostCommentList = user.getLikePostCommentList();
        List<PostComment> dislikePostCommentList = user.getDislikePostCommentList();
        //해당 post를 이미 dislike 한 경우 - 제거
        if (dislikeUserList.contains(user)) {
            postcomment.setLikeCount(postcomment.getLikeCount() + 1);

            dislikePostCommentList.remove(postcomment);
            dislikeUserList.remove(user);
        }
        //해당 post를 이미 like 한 경우 - 제거하고 추가
        else if (likeUserList.contains(user)) {
            postcomment.setLikeCount(postcomment.getLikeCount() - 2);

            likeUserList.remove(user);
            likePostCommentList.remove(postcomment);
            dislikeUserList.add(user);
            dislikePostCommentList.add(postcomment);
        }
        // 처음 dislike 하는 경우-추가
        else {
            postcomment.setLikeCount(postcomment.getLikeCount() - 1);

            dislikeUserList.add(user);
            dislikePostCommentList.add(postcomment);
        }
        postCommentRepository.save(postcomment);
        userRepository.save(user);
    }


}
