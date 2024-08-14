package com.example.comus.domain.user.dto.response;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record MainPageUserresponseDto(
        Long id,
        String name,
        String imageUrl,
        LocalTime todayChatTime
) {
}
