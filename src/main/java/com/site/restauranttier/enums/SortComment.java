package com.site.restauranttier.enums;

public enum SortComment {
    POPULAR("popular"), LATEST("latest");

    private final String value;

    SortComment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
