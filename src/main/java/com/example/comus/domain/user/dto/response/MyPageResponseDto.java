package com.example.comus.domain.user.dto.response;

import com.example.comus.domain.answer.dto.response.StatisticResponseDto;
import com.example.comus.domain.user.entity.User;

public record MyPageResponseDto(
        UserInfoResponseDto userInfo,
        StatisticResponseDto answerStatistic
) {
}
