package com.site.restauranttier.etc;

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
