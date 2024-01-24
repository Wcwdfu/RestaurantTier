package com.site.restauranttier.etc;

import com.site.restauranttier.entity.PostComment;
import com.site.restauranttier.entity.PostPhoto;
import com.site.restauranttier.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostDTO {
    Integer postId;
    String postTitle;
    String postBody;
    String status;
    User user;
    String photoImgUrl;
    LocalDateTime createdAt;

    List<PostPhoto> postPhotoList = new ArrayList<>();
    List<PostComment> postCommentList = new ArrayList<>();

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

    public void setUser(User user) {
        this.user = user;
    }

    public void setPhotoImgUrl(String photoImgUrl) {
        this.photoImgUrl = photoImgUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setPostPhotoList(List<PostPhoto> postPhotoList) {
        this.postPhotoList = postPhotoList;
    }

    public void setPostCommentList(List<PostComment> postCommentList) {
        this.postCommentList = postCommentList;
    }

    public Integer getPostId() {
        return postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public String getPhotoImgUrl() {
        return photoImgUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<PostPhoto> getPostPhotoList() {
        return postPhotoList;
    }

    public List<PostComment> getPostCommentList() {
        return postCommentList;
    }
}
