package com.example.comus.domain.question.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerType implements EnumConverter.KoreanEnum {
    SENTENCE("대화형"),
    MULTIPLE_CHOICE("선택형");

    private final String korean;

    @Override
    @JsonValue
    public String getKorean() {
        return korean;
    }

    @JsonCreator
    public static AnswerType fromKorean(String korean) {
        return new EnumConverter<>(AnswerType.class) {
        }.fromKorean(korean);
    }
}
