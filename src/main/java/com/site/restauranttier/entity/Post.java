package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "posts_tbl")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer postId;

    String postTitle;
    String postBody;
    String status;
    String postCategory;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Integer postVisitCount;

    public Post(String postTitle, String postBody, String postCategory, String status, LocalDateTime createdAt) {
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.postCategory = postCategory;
        this.status = status;
        this.createdAt = createdAt;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "post")
    List<PostComment> postCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    List<PostScrap> postScrapList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    List<PostPhoto> postPhotoList = new ArrayList<>();

    public Post() {

    }

    @ManyToMany
    @JoinTable(name="post_likes_tbl",joinColumns = @JoinColumn(name="post_id"),inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> dislikeUserList = new ArrayList<>();
    @ManyToMany
    @JoinTable(name="post_dislikes_tbl",joinColumns = @JoinColumn(name="post_id"),inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> likeUserList = new ArrayList<>();

}
