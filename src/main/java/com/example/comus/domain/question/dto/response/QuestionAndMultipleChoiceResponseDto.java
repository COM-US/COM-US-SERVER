package com.example.comus.domain.question.dto.response;

import java.util.List;

public record QuestionAndMultipleChoiceResponseDto(
        QuestionResponseDto question,
        List<String> multipleChoice
) {
}
