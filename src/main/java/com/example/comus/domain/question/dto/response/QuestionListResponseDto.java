package com.example.comus.domain.question.dto.response;

import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.entity.QuestionCategory;

public record QuestionListResponseDto(
        Long id,
        QuestionCategory category,
        AnswerType answerType,
        String questionContent,
        int answerCount,
        boolean isLiked
) {
    public static QuestionListResponseDto from(Question question, int answerCount, boolean isLiked) {
        return new QuestionListResponseDto(
                question.getId(),
                question.getCategory(),
                question.getAnswerType(),
                question.getQuestionContent(),
                answerCount,
                isLiked
        );
    }
}
