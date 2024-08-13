package com.example.comus.domain.answer.dto.request;

public record AnswerRequestDto(
        Long questionId,
        String answerContent
) {
}
