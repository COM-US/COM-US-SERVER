package com.example.comus.domain.question.controller;

import com.example.comus.domain.question.dto.request.QuestionRequestDto;
import com.example.comus.domain.question.dto.response.QuestionAndMultipleChoiceResponseDto;
import com.example.comus.domain.question.dto.response.QuestionListResponseDto;
import com.example.comus.domain.question.dto.response.QuestionResponseDto;
import com.example.comus.domain.question.entity.QuestionCategory;
import com.example.comus.domain.question.entity.SortType;
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

    // 카테고리별 질문리스트 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getQuestion(
            @UserId Long userId,
            @RequestParam(value = "category", required = false, defaultValue = "") QuestionCategory category,
            @RequestParam(value = "sort", required = false, defaultValue = "DEFAULT") SortType sort
    ) {
        List<QuestionListResponseDto> questionList = questionService.getQuestionList(userId, category, sort);
        return SuccessResponse.ok(questionList);
    }

    // 질문 상세 조회 (선택형 답변 조회 포함)
    @PostMapping("/detail")
    public ResponseEntity<SuccessResponse<?>> getQuestionDatail(@RequestBody QuestionRequestDto questionRequest) {
        Long questionId = questionRequest.isRandom() && questionRequest.questionId() == null
                ? questionService.getRandomQuestionId()
                : questionRequest.questionId();

        QuestionResponseDto question = questionService.getQuestion(questionId);
        List<String> multipleChoice = questionService.getQuestionDatail(questionId);

        QuestionAndMultipleChoiceResponseDto questionAndMultipleChoice = new QuestionAndMultipleChoiceResponseDto(question, multipleChoice);
        return SuccessResponse.ok(questionAndMultipleChoice);
    }

    // 질문 찜하기
    @PostMapping("{question_id}/like")
    public ResponseEntity<SuccessResponse<?>> likeQuestion(@UserId Long userId, @PathVariable("question_id") Long questionId) {
        questionService.likeQuestion(userId, questionId);
        return SuccessResponse.created(null);
    }

    // 질문 찜하기 취소
    @PostMapping("{question_id}/unlike")
    public ResponseEntity<SuccessResponse<?>> unlikeQuestion(@UserId Long userId, @PathVariable("question_id") Long questionId) {
        questionService.unlikeQuestion(userId, questionId);
        return SuccessResponse.ok(null);
    }
}
