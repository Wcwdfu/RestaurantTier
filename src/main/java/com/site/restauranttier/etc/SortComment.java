package com.site.restauranttier.etc;

import lombok.Getter;

@Getter
public enum SortComment {
    POPULAR("popular"), LATEST("latest");

    private final String value;

    SortComment(String value) {
        this.value = value;
    }

}
