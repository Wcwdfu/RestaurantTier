package com.site.restauranttier;

import jakarta.persistence.*;

@Entity
@Table(name="evaluation_items_tbl")
public class EvaluationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;

    private String itemName;
    // Getters and Setters...
}
