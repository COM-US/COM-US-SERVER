package com.example.comus.domain.answer.dto.response;

import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.entity.QuestionCategory;

import java.util.List;


public record AnswerByQuestionResponseDto(
        Long questionId,
        QuestionCategory category,
        AnswerType answerType,
        String questionContent,
        int answerCount,
        List<AnswerResponseDto> answers
) {
    public static AnswerByQuestionResponseDto from(Question question, List<AnswerResponseDto> answers) {
        return new AnswerByQuestionResponseDto(
                question.getId(),
                question.getCategory(),
                question.getAnswerType(),
                question.getQuestionContent(),
                answers.size(),
                answers
        );
    }
}
