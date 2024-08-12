package com.example.comus.domain.question.controller;

import com.example.comus.domain.question.service.QuestionService;
import com.example.comus.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/question")
@RestController
public class QuestionController {
    private final QuestionService questionService;

}
