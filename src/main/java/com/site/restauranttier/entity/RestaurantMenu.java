package com.site.restauranttier.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="restaurant_id")
    Restaurant restaurant;

    private String menuName;
    private String menuPrice;
    private String naverType;
    private String menuImgUrl;
}
