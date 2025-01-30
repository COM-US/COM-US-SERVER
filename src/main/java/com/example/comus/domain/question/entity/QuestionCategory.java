package com.example.comus.domain.question.entity;

public enum QuestionCategory {
    DAILY ("일상"),
    SCHOOL("학교"),
    FRIEND("친구"),
    FAMILY("가족"),
    HOBBY("관심사"),
    RANDOM("랜덤");

    private final String korean;

    QuestionCategory(String korean) {
        this.korean = korean;
    }

    public static QuestionCategory fromString(String source) {
        for (QuestionCategory questionCategory : QuestionCategory.values()) {
            if (questionCategory.name().equals(source)) {
                return questionCategory;
            }
        }
        return null;
    }
}
