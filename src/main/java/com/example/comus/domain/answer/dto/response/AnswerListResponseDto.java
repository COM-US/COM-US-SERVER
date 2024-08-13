package com.example.comus.domain.answer.dto.response;

import com.example.comus.domain.question.dto.response.QuestionListResponseDto;
import com.example.comus.domain.question.dto.response.QuestionResponseDto;

import java.util.List;

public record AnswerListResponseDto(
        QuestionListResponseDto question,
        List<AnswerResponseDto> answerList
) {
}
