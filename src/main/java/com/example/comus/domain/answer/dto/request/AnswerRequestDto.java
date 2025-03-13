package com.example.comus.domain.answer.dto.request;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.user.entity.User;

public record AnswerRequestDto(
        Long questionId,
        String answerContent
) {
    public Answer toEntity(User user, Question question) {
        return Answer.builder()
                .user(user)
                .question(question)
                .questionCategory(question.getCategory())
                .answerContent(answerContent)
                .isUsed(false)
                .build();
    }
}
