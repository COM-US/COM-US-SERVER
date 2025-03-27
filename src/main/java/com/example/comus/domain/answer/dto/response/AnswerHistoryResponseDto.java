package com.example.comus.domain.answer.dto.response;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.QuestionCategory;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record AnswerHistoryResponseDto(
        String answerTime,
        QuestionCategory category,
        AnswerType answerType,
        String questionContent,
        String answerContent
) {
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm", Locale.KOREAN);

    public static AnswerHistoryResponseDto from(Answer answer) {
        return new AnswerHistoryResponseDto(
                answer.getCreatedAt().format(TIME_FORMATTER),
                answer.getQuestion().getCategory(),
                answer.getAnswerType(),
                answer.getQuestion().getQuestionContent(),
                answer.getAnswerContent()
        );
    }
}
