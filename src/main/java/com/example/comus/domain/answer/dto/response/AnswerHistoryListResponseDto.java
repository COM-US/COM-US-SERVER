package com.example.comus.domain.answer.dto.response;

import java.util.List;

public record AnswerHistoryListResponseDto(
        String answerDate,
        List<AnswerHistoryResponseDto> answers
) {
    public static AnswerHistoryListResponseDto from(String date, List<AnswerHistoryResponseDto> answers) {
        return new AnswerHistoryListResponseDto(date, answers);
    }
}
