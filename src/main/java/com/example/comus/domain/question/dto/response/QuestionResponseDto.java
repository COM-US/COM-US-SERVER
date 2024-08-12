package com.example.comus.domain.question.dto.response;

import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.Category;

public record QuestionResponseDto(
        Long id,
        Category category,
        AnswerType answerType,
        String questionContent){

}
