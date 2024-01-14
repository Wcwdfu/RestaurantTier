package com.site.restauranttier;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userPassword;
    private Integer userNum;
    private String userName;
    private String userSex;
    private Date userBirthday;
    private String userEmail;
    private String userNickname;
    private String status;
    private Date createdAt;
    private Date updatedAt;
}

