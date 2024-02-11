package com.site.restauranttier.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.site.restauranttier.etc.EnumSituation;
import com.site.restauranttier.etc.EnumTier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "restaurants_tbl")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restaurantId;


    private String restaurantName;
    private String restaurantType;
    private String restaurantPosition;
    private String restaurantAddress;
    private String restaurantTel;
    @Column(unique = true)
    private String restaurantUrl;
    private String restaurantImgUrl;
    private Integer restaurantVisitCount;
    private Integer visitCount;

    private String restaurantCuisine;
    private String restaurantLatitude;
    private String restaurantLongitude;

    private String status;
    private LocalDateTime createdAt;

    public Restaurant(String restaurantName, String restaurantType, String restaurantUrl, Integer visitCount,String restaurantCuisine, String status, LocalDateTime createdAt) {
        this.restaurantName = restaurantName;
        this.restaurantType = restaurantType;
        this.restaurantUrl = restaurantUrl;
        this.restaurantVisitCount=visitCount;
        this.restaurantCuisine = restaurantCuisine;
        this.status = status;
        this.createdAt = createdAt;
    }

    private LocalDateTime updatedAt;


    public Restaurant(){

    }
    // 다른 테이블과의 관계 매핑
    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "restaurant_hashtag_relations_tbl", joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name="hashtag_id"))
    List<RestaurantHashtag> restaurantHashtagList = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "restaurant_situation_relations_tbl", joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name="situation_id"))
    List<Situation> situationList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private List<Evaluation> evaluationList=new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private List<RestaurantComment> restaurantCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private List<RestaurantFavorite> restaurantFavorite = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private List<RestaurantMenu> restaurantMenuList = new ArrayList<>();

    public String getTierImgUrl(Integer tier) {
        String url = "/img/tier/" + tier.toString() + "tier.png";
        return url;
    }
    public String getCuisineImgUrl(String cuisine) {
        return "/img/tier/cuisine/" + getSubstringBefore(cuisine, '/') + ".png";
    }
    public String getSubstringBefore(String input, char delimiter) {
        int index = input.indexOf(delimiter);
        if (index != -1) {
            return input.substring(0, index);
        }
        return input; // delimiter가 없는 경우에는 원본 문자열 그대로 반환
    }

    public List<String> getSituationImgUrlList() {
        List<String> imgUrlList = new ArrayList<>();
        for (Situation situation: this.getSituationList()) {
            String situationUrl = "/img/tier/" + situation.getSituationName() + ".png";
            imgUrlList.add(situationUrl);
        }
        return imgUrlList;
    }
}
