package com.example.comus.domain.answer.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record AnswerResponseDto(
        Long id,
        String answerContent,
        Long questionId,
        String createdAt
) {
}
