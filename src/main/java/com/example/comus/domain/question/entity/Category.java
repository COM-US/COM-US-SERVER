package com.example.comus.domain.question.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category implements EnumConverter.KoreanEnum {
    DAILY("일상"),
    SCHOOL("학교"),
    FRIEND("친구"),
    FAMILY("가족"),
    HOBBY("관심사"),
    RANDOM("랜덤");

    private final String korean;

    Category(String korean) {
        this.korean = korean;
    }

    @Override
    @JsonValue
    public String getKorean() {
        return korean;
    }

    @JsonCreator
    public static Category fromKorean(String korean) {
        return EnumConverter.fromKorean(Category.class, korean);
    }
}
