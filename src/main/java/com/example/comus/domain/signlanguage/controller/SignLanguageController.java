package com.example.comus.domain.signlanguage.controller;

import com.example.comus.domain.signlanguage.dto.response.SignLanguageInfoResponseDto;
import com.example.comus.domain.signlanguage.service.SignLanguageService;
import com.example.comus.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/sign-language")
@RestController
public class SignLanguageController {

      private final SignLanguageService signLanguageService;

      @GetMapping
        public ResponseEntity<SuccessResponse<?>> getSignLanguageInfo(@RequestParam String answer) {
           List<SignLanguageInfoResponseDto> signLanguageInfo= signLanguageService.getSignLanguage(answer);
            return SuccessResponse.ok(signLanguageInfo);
        }

}
