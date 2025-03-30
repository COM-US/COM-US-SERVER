package com.example.comus.domain.answer.dto.response;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.question.entity.AnswerType;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record AnswerResponseDto(
        Long answerId,
        String answerDate,
        AnswerType answerType,
        String answerContent
) {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREAN);

    public static AnswerResponseDto from(Answer answer) {
        return new AnswerResponseDto(
                answer.getId(),
                answer.getCreatedAt().format(DATE_FORMATTER),
                answer.getAnswerType(),
                answer.getAnswerContent()
        );
    }

}
