package com.example.comus.domain.answer.dto.response;

public record StatisticResponseDto(
        double sentenceRatio,

        double multipleChoiceRatio,
        double dailyQuestionRatio,
        double schoolQuestionRatio,
        double hobbyQuestionRatio,
        double familyQuestionRatio,
        double friendQuestionRatio,
        double randomQuestionRatio
) {
}
