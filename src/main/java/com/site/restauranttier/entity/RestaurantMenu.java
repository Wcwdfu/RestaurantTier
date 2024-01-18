package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="restaurant_menus_tbl")
public class RestaurantMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;

    @OneToMany(mappedBy = "restaurantComment")
    private List<RestaurantCommentlike> restaurantCommentlikeList=new ArrayList<>();

    private String menuName;
    private String menuPrice;
    private String naverType;
    private String menuImgUrl;
}
