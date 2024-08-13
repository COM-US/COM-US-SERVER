package com.example.comus.domain.signlanguage.dto.response;

public record SignLanguageInfoResponseDto(
        Long id,
        String signLanguageName,
        String signLanguageVideoUrl,
        String signLanguageDescription
) {
}
