package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="post_comments_tbl")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer commentId;

    String commentBody;
    Integer parentCommentId;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

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
}
