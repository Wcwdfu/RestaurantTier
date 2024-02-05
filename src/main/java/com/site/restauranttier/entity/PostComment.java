package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name="post_comments_tbl")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer commentId;
    String commentBody;
    String status;
    @ManyToOne
    @JoinColumn(name="parent_comment_id")
    PostComment parentComment;

    @OneToMany(mappedBy = "parentComment")
    List<PostComment> repliesList = new ArrayList<>();
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Integer likeCount=0;

    public PostComment(String commentBody, String status, LocalDateTime createdAt, Post post, User user) {
        this.commentBody = commentBody;
        this.status = status;
        this.createdAt = createdAt;
        this.post = post;
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="post_id")
    Post post;
    @ManyToOne
    @JoinColumn(name="user_id")
    User user;

    public PostComment() {

    }

    @ManyToMany
    @JoinTable(name="comment_likes_tbl",joinColumns = @JoinColumn(name="comment_id"),inverseJoinColumns = @JoinColumn(name="user_id"))
    List<User> likeUserList = new ArrayList<>();
    @ManyToMany
    @JoinTable(name="comment_dislikes_tbl",joinColumns = @JoinColumn(name="comment_id"),inverseJoinColumns = @JoinColumn(name="user_id"))
    List<User> dislikeUserList = new ArrayList<>();
}
