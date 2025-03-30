package com.example.comus.domain.user.dto.response;

import com.example.comus.domain.block.dto.response.BlockResponseDto;
import com.example.comus.domain.question.dto.response.QuestionCountResponseDto;
import com.example.comus.domain.question.dto.response.RandomQuestionResponseDto;

import java.util.List;

public record MainPageResponseDto(
        UserInfoResponseDto userInfo,
        RandomQuestionResponseDto randomQuestion,
        List<BlockResponseDto> blocks,
        List<QuestionCountResponseDto> questionCounts
) {
    public static MainPageResponseDto of(UserInfoResponseDto userInfo, RandomQuestionResponseDto randomQuestion, List<BlockResponseDto> blocks, List<QuestionCountResponseDto> questionCounts) {
        return new MainPageResponseDto(userInfo, randomQuestion, blocks, questionCounts);
    }
}
