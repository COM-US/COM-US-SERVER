package com.example.comus.domain.user.dto.response;

import com.example.comus.domain.user.entity.User;
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
    public static UserInfoResponseDto from(User user){
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .totalChatCount(user.getTotalChatCount())
                .build();
    }
}
