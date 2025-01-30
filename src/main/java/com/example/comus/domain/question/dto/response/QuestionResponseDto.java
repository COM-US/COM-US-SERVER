package com.example.comus.domain.question.dto.response;

import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.QuestionCategory;

import java.time.LocalDate;

public record QuestionResponseDto(
        Long id,
        LocalDate answerDate,
        QuestionCategory category,
        AnswerType answerType,
        String questionContent
){
}
