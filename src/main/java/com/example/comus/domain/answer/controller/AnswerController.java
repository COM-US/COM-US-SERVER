package com.example.comus.domain.answer.controller;

import com.example.comus.domain.answer.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/answer")
@RestController
public class AnswerController {
    private final AnswerService answerService;


}
