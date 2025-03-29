package com.example.comus.domain.block.dto.request;

import com.example.comus.domain.question.entity.QuestionCategory;

public record BlockPlaceRequestDto(
        QuestionCategory questionCategory,
        int[][] blockPlace
) {
}
