package com.site.restauranttier;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="evaluations_tbl")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int evaluationId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String evaluationComment;
    private int evaluationScore;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Getters and Setters...
}
