package com.example.comus.domain.signlanguage.controller;

import com.example.comus.domain.signlanguage.service.SignLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/sign-language")
@RestController
public class SignLanguageController {

      private final SignLanguageService signLanguageService;

}
