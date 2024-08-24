package com.example.comus.domain.signlanguage.dto.response;

import java.util.List;

public record SignLanguageInfoAndDateResponseDto (
        List<SignLanguageInfoResponseDto> signLanguageInfo,
        String answerDate
){
}
