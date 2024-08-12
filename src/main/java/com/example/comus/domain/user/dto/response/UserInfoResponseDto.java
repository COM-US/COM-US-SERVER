package com.example.comus.domain.user.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record UserInfoResponseDto(
        Long id,
        String name,
        String imageUrl,
        LocalTime todayChatTime,
        LocalTime totalChatTime,
        int totalChatCount

) {
}
