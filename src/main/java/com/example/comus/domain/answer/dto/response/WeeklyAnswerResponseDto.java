package com.example.comus.domain.answer.dto.response;

import com.example.comus.domain.question.entity.QuestionCategory;

public record WeeklyAnswerResponseDto(
        String answerDay,
        QuestionCategory category
) {
}
