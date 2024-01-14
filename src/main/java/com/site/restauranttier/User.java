package com.site.restauranttier;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name="users_tbl")
public class User {
    @Id
    private String userId;

    @OneToMany(mappedBy ="user")
    private List<RestaurantComment> restaruantCommentList;
    @OneToMany(mappedBy = "user")
    private List<Evaluation> EvaluationList;
    private String userPassword;
    private int userNum;
    private String userName;
    private String userSex;
    private LocalDate userBirthday;
    private String userEmail;
    private String userNickname;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

