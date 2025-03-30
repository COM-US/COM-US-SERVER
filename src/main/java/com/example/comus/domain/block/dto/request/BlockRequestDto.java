package com.example.comus.domain.block.dto.request;

import com.example.comus.domain.question.entity.QuestionCategory;

import java.util.List;

public record BlockRequestDto(
        QuestionCategory category,
        List<BlockPlaceRequestDto> blockPlace
) {
}
