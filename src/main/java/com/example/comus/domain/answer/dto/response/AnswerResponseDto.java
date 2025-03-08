package com.example.comus.domain.answer.dto.response;

import com.example.comus.domain.answer.entity.Answer;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record AnswerResponseDto(
        Long id,
        String answerContent,
        Long questionId,
        String createdAt
) {
    public static AnswerResponseDto from(Answer answer, String formattedDate ) {
        return new AnswerResponseDto(
                answer.getId(),
                answer.getAnswerContent(),
                answer.getQuestion().getId(),
                formattedDate
        );
    }
}
