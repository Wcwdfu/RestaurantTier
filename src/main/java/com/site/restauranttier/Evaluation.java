package com.site.restauranttier;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluationId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String evaluationComment;
    private Integer evaluationScore;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    // Getters and Setters...
}
