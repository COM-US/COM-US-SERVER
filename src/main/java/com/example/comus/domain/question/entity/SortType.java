package com.example.comus.domain.question.entity;

public enum SortType {
    DEFAULT("전체"),
    MOST_CHAT("대화 많은 순"),
    LEAST_CHAT("대화 적은 순");


    private final String korean;

    SortType(String korean) {
        this.korean = korean;
    }

    public static SortType fromString(String source) {
        for (SortType sortType : SortType.values()) {
            if (sortType.name().equals(source)) {
                return sortType;
            }
        }
        return null;
    }
}
