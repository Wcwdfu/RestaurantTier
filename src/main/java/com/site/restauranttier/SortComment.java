package com.site.restauranttier;

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
