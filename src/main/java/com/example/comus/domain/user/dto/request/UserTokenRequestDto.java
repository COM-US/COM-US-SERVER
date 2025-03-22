package com.example.comus.domain.user.dto.request;

public record UserTokenRequestDto(
        String accessToken,
        String refreshToken
) {
}
