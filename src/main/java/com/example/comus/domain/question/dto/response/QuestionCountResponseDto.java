package com.example.comus.domain.question.dto.response;

import com.example.comus.domain.question.entity.QuestionCategory;

public record QuestionCountResponseDto(
        QuestionCategory category,
        int questionTotalCount,
        int questionAnsweredCount,
        String count,
        String percentage
) {
}
