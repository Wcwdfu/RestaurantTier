package com.site.restauranttier.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.site.restauranttier.user.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users_tbl")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(unique = true, nullable = false)
    private String userTokenId;

    private String userPassword;
    @Column(unique = true, nullable = false)
    private String userEmail;
    @Column(unique = true, nullable = false)

    private String userNickname;
    @Column(nullable = false)

    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    @Column(nullable = false)
    private String loginApi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<RestaurantComment> restaruantCommentList = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Evaluation> evaluationList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<RestaurantFavorite> restaurantFavoriteList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<RestaurantCommentlike> restaurantCommentlikeList = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<RestaurantCommentdislike> restaurantCommentdislikeList = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<PostComment> postCommentList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<PostScrap> scrapList = new ArrayList<>();

    @Builder
    public User(String userTokenId, String loginApi, String userPassword, String userEmail, String userNickname, UserRole userRole, String status, LocalDateTime createdAt) {
        this.userTokenId = userTokenId;
        this.loginApi = loginApi;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.userRole = userRole;
        this.status = status;
        this.createdAt = createdAt;
    }

    public User update(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public String getRoleKey() {
        return this.userRole.getValue();
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "dislikeUserList")
    private List<Post> dislikePostList = new ArrayList<>();
    @JsonIgnore
    @ManyToMany(mappedBy = "likeUserList")
    private List<Post> likePostList = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "dislikeUserList")
    private List<PostComment> dislikePostCommentList = new ArrayList<>();
    @JsonIgnore
    @ManyToMany(mappedBy = "likeUserList")
    private List<PostComment> likePostCommentList = new ArrayList<>();

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        return Objects.equals(userId, that.userId) &&
                Objects.equals(userTokenId, that.userTokenId)&&
                Objects.equals(userPassword, that.userPassword)&&
                Objects.equals(userEmail, that.userEmail)&&
                Objects.equals(userNickname, that.userNickname)&&
                Objects.equals(status, that.status)&&
                Objects.equals(userRole, that.userRole)&&
                Objects.equals(loginApi, that.loginApi)&&
                Objects.equals(createdAt, that.createdAt)&&
                Objects.equals(updatedAt, that.updatedAt);
    }*/
}

