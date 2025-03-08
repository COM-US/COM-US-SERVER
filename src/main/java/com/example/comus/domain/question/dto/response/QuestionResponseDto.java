package com.example.comus.domain.question.dto.response;

import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.entity.QuestionCategory;

import java.time.LocalDate;

public record QuestionResponseDto(
        Long id,
        LocalDate answerDate,
        QuestionCategory category,
        AnswerType answerType,
        String questionContent
){
    public static QuestionResponseDto from(Question question){
        return new QuestionResponseDto(
                question.getId(),
                LocalDate.now(),
                question.getCategory(),
                question.getAnswerType(),
                question.getQuestionContent()
        );
    }
}
