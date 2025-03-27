package com.example.comus.domain.question.entity;

public enum AnswerType{
    SENTENCE("대화형"),
    MULTIPLE_CHOICE("선택형"),
    BOTH("대화형 + 선택형");

    private final String korean;

    AnswerType(String korean) {
        this.korean = korean;
    }

    public static AnswerType fromString(String source) {
        for (AnswerType answerType : AnswerType.values()) {
            if (answerType.name().equals(source)) {
                return answerType;
            }
        }
        return null;
    }
}
