package com.example.comus.domain.block.dto.response;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.QuestionCategory;

import java.util.List;

public record BlockInfoResponseDto(
        Long answerId,
        QuestionCategory category,
        AnswerType answerType,
        String questionContent,
        String answerContent

) {
    public static BlockInfoResponseDto from(Answer answer) {
        return new BlockInfoResponseDto(
                answer.getId(),
                answer.getQuestion().getCategory(),
                answer.getAnswerType(),
                answer.getQuestion().getQuestionContent(),
                answer.getAnswerContent()
        );

    }
}
