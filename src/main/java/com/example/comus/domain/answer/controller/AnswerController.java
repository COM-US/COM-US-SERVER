package com.example.comus.domain.answer.controller;

import com.example.comus.domain.answer.dto.request.AnswerRequestDto;
import com.example.comus.domain.answer.dto.response.AnswerListResponseDto;
import com.example.comus.domain.answer.dto.response.AnswerResponseDto;
import com.example.comus.domain.answer.service.AnswerService;
import com.example.comus.domain.question.dto.response.QuestionListResponseDto;
import com.example.comus.domain.question.service.QuestionService;
import com.example.comus.global.common.SuccessResponse;
import com.example.comus.global.config.auth.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/answer")
@RestController
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<?> createAnswer(@UserId Long userId, @RequestBody AnswerRequestDto answerRequest) {
        answerService.createAnswer(userId, answerRequest);
        return SuccessResponse.created(null);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getAnswer(@UserId Long userId, @PathVariable Long questionId) {
        QuestionListResponseDto question = questionService.getQuestionAndCount(questionId);
        List<AnswerResponseDto> answerlist = answerService.getAnswer(userId, questionId);
        AnswerListResponseDto answerListResponseDto = new AnswerListResponseDto(question, answerlist);
        return SuccessResponse.ok(answerListResponseDto);
    }

}
