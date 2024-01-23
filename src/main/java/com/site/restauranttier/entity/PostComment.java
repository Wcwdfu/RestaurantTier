package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="post_comments_TBL")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer commentId;

    String commentBody;
    String parentCommentId;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="post_id")
    Post post;
    @ManyToOne
    @JoinColumn(name="user_id")
    User user;

}
