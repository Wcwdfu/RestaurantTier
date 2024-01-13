package com.site.restauranttier;

import jakarta.persistence.*;

@Entity
public class SituationCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;
}
