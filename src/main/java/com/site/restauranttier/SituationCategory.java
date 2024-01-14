package com.site.restauranttier;

import jakarta.persistence.*;

@Entity
@Table(name="situation_categories")
public class SituationCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    private String categoryName;
}
