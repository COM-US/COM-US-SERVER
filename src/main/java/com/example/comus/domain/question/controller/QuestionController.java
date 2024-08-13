package com.example.comus.domain.question.controller;

import com.example.comus.domain.question.dto.response.QuestionAndMultipleChoiceResponseDto;
import com.example.comus.domain.question.dto.response.QuestionResponseDto;
import com.example.comus.domain.question.entity.Category;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.service.QuestionService;
import com.example.comus.global.common.SuccessResponse;
import com.example.comus.global.config.auth.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/question")
@RestController
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getQuestion(@RequestParam(value = "category", required = false) Category category) {
        List<QuestionResponseDto> questionList;

        if (category == null) {
            // category 파라미터가 없을 경우 모든 질문을 반환
            questionList = questionService.getAllQuestions();
        } else {
            // category 파라미터가 있을 경우 해당 카테고리의 질문을 반환
            questionList = questionService.getQuestions(category);
        }

        return SuccessResponse.ok(questionList);
    }

    @GetMapping("{question_id}/multiple-choice")
    public ResponseEntity<SuccessResponse<?>> getMultipleChoiceAnswer(@PathVariable("question_id") Long questionId) {
        QuestionResponseDto question = questionService.getQuestion(questionId);
        List<String> answerList = questionService.getMultipleChoiceAnswer(questionId);
        QuestionAndMultipleChoiceResponseDto questionAndMultipleChoice = new QuestionAndMultipleChoiceResponseDto(question, answerList);
        return SuccessResponse.ok(questionAndMultipleChoice);
    }
}
