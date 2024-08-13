package com.example.comus.domain.answer.controller;

import com.example.comus.domain.answer.dto.request.AnswerRequestDto;
import com.example.comus.domain.answer.service.AnswerService;
import com.example.comus.global.common.SuccessResponse;
import com.example.comus.global.config.auth.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/answer")
@RestController
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<?> createAnswer(@UserId Long userId, @RequestBody AnswerRequestDto answerRequest) {
        answerService.createAnswer(userId,answerRequest);
        return SuccessResponse.created(null);
    }

}
