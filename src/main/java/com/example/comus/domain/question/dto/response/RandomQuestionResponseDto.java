package com.example.comus.domain.question.dto.response;

import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.entity.QuestionCategory;

public record RandomQuestionResponseDto(
        Long questionId,
        String questionContent,
        QuestionCategory category,
        AnswerType answerType
) {
    public static RandomQuestionResponseDto from(Question question) {
        return new RandomQuestionResponseDto(
                question.getId(),
                question.getQuestionContent(),
                question.getCategory(),
                question.getAnswerType()
        );
    }
}
