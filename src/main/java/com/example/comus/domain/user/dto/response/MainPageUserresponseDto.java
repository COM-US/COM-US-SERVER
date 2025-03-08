package com.example.comus.domain.user.dto.response;

import com.example.comus.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record MainPageUserresponseDto(
        Long id,
        String name,
        String imageUrl,
        LocalTime todayChatTime
) {
    public static MainPageUserresponseDto from(User user){
        return MainPageUserresponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .todayChatTime(user.getTodayChatTime())
                .build();
    }
}
