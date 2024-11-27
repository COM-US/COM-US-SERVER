package com.example.comus.domain.user.controller;

import com.example.comus.domain.user.dto.response.UserTokenResponseDto;
import com.example.comus.domain.user.service.OauthService;
import com.example.comus.domain.user.service.UserService;
import com.example.comus.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class OauthController {

    private final OauthService oauthService;
    private final UserService userService;

    @PostMapping("/google-login")
    ResponseEntity<SuccessResponse<?>> googleLogin(@RequestParam String code) {
        Long userId  = oauthService.socialLogin(code);
        String accessToken = userService.issueNewAccessToken(userId);
        String refreshToken = userService.issueNewRefreshToken(userId);
        UserTokenResponseDto userTokenResponseDto = UserTokenResponseDto.of(accessToken, refreshToken);
        return SuccessResponse.ok(userTokenResponseDto);

    }
}
