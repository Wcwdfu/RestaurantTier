package com.site.restauranttier.entity;

import jakarta.persistence.*;

@Entity
public class PostScraps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer scrapId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    Post post;

    //created 가 필요한가? 없으면 ManyToMany로 가능.

}
