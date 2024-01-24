package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="posts_tbl")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer postId;

    String postTitle;
    String postBody;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public Post(Integer postId, String postTitle, String postBody, String status, LocalDateTime createdAt) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.status = status;
        this.createdAt = createdAt;
    }

    @ManyToOne
    @JoinColumn(name="user_id")
    User user;

    @OneToMany(mappedBy = "post")
    List<PostComment> postCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    List<PostScrap> postScrapList = new ArrayList<>();

    public Integer getPostId() {
        return postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public List<PostComment> getPostCommentList() {
        return postCommentList;
    }

    public List<PostScrap> getPostScrapList() {
        return postScrapList;
    }

    public List<PostPhoto> getPostPhotoList() {
        return postPhotoList;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPostCommentList(List<PostComment> postCommentList) {
        this.postCommentList = postCommentList;
    }

    public void setPostScrapList(List<PostScrap> postScrapList) {
        this.postScrapList = postScrapList;
    }

    public void setPostPhotoList(List<PostPhoto> postPhotoList) {
        this.postPhotoList = postPhotoList;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    @OneToMany(mappedBy = "post")
    List<PostPhoto> postPhotoList = new ArrayList<>();

    public Post() {

    }
}
