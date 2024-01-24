package com.site.restauranttier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@Setter
// 한 사용자가 한 식당을 중복 평가 할 수 없음
@Table(name="evaluations_tbl")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer evaluationId;

    private String evaluationComment;

    private Double evaluationScore;

    private String status="ACTIVE";

    public Evaluation(Restaurant restaurant,User user, Double evaluationScore) {
        this.restaurant = restaurant;
        this.user = user;
        this.evaluationScore = evaluationScore;
        this.createdAt = LocalDateTime.now();
    }

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "evaluation")
    private List<EvaluationItemScore> EvaluationItemScoreList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    public Evaluation() {

    }
}
