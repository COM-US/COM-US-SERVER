package com.example.comus.domain.answer.controller;

import com.example.comus.domain.answer.dto.request.AnswerRequestDto;
import com.example.comus.domain.answer.dto.response.AnswerListResponseDto;
import com.example.comus.domain.answer.dto.response.AnswerResponseDto;
import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.answer.service.AnswerService;
import com.example.comus.domain.block.entity.Block;
import com.example.comus.domain.block.service.BlockService;
import com.example.comus.domain.question.dto.response.QuestionListResponseDto;
import com.example.comus.domain.question.service.QuestionService;
import com.example.comus.domain.signlanguage.dto.response.SignLanguageInfoAndDateResponseDto;
import com.example.comus.domain.signlanguage.dto.response.SignLanguageInfoResponseDto;
import com.example.comus.domain.signlanguage.service.SignLanguageService;
import com.example.comus.global.common.SuccessResponse;
import com.example.comus.global.config.auth.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/answer")
@RestController
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final BlockService blockService;
    private final SignLanguageService signLanguageService;


    @PostMapping
    public ResponseEntity<?> createAnswer(@UserId Long userId, @RequestBody AnswerRequestDto answerRequest) {
        long anwserId = answerService.createAnswer(userId, answerRequest);
        blockService.save(userId, anwserId);

        String answer = answerRequest.answerContent();
        List<SignLanguageInfoResponseDto> signLanguageInfo= signLanguageService.getSignLanguage(answer);
        LocalDateTime answerDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        String formattedDate = answerDate.format(formatter);
        SignLanguageInfoAndDateResponseDto signLanguageInfoAndDate = new SignLanguageInfoAndDateResponseDto(signLanguageInfo, formattedDate);

        return SuccessResponse.ok(signLanguageInfoAndDate);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getAnswer(@UserId Long userId, @PathVariable Long questionId) {
        QuestionListResponseDto question = questionService.getQuestionAndCount(userId, questionId);
        List<AnswerResponseDto> answerlist = answerService.getAnswer(userId, questionId);
        AnswerListResponseDto answerListResponseDto = new AnswerListResponseDto(question, answerlist);
        return SuccessResponse.ok(answerListResponseDto);
    }

}
