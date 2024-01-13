package com.site.restauranttier;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@Entity
@Table(name = "users_tbl")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_num")
    private Integer num;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_sex")
    private String sex;

    @Column(name = "user_birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;



}
