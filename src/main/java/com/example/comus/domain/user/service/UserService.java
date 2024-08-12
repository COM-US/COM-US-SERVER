package com.example.comus.domain.user.service;

import com.example.comus.domain.user.dto.response.UserTokenResponseDto;
import com.example.comus.global.config.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final JwtProvider jwtProvider;

    public String issueNewAccessToken(Long memberId) {
        return jwtProvider.getIssueToken(memberId, true);
    }
    private String issueNewRefreshToken(Long memberId) {
        return jwtProvider.getIssueToken(memberId, false);
    }

    public UserTokenResponseDto getToken(Long userId) {
        String accessToken = issueNewAccessToken(userId);
        String refreshToken = issueNewRefreshToken(userId);
        return UserTokenResponseDto.of(accessToken, refreshToken);
    }
}
