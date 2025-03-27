package com.example.comus.domain.answer.dto.request;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.user.entity.User;

public record AnswerRequestDto(
        Long questionId,
        AnswerType answerType,
        String answerContent
) {
    public Answer toEntity(User user, Question question) {
        return Answer.builder()
                .user(user)
                .question(question)
                .questionCategory(question.getCategory())
                .answerType(answerType)
                .answerContent(answerContent)
                .isUsed(false)
                .build();
    }
}
