package com.example.comus.domain.user.controller;

import com.example.comus.domain.answer.service.AnswerService;
import com.example.comus.domain.user.dto.request.LoginRequestDto;
import com.example.comus.domain.user.dto.request.UserTokenRequestDto;
import com.example.comus.domain.user.dto.response.MainPageResponseDto;
import com.example.comus.domain.user.dto.response.UserTokenResponseDto;
import com.example.comus.domain.user.service.UserService;
import com.example.comus.global.common.SuccessResponse;
import com.example.comus.global.config.auth.UserId;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;
    private final AnswerService answerService;

    // 임시 토큰 발급
    @PostMapping("/token/{userId}")
    public ResponseEntity<SuccessResponse<?>> getToken(@PathVariable(name = "userId") Long userId) {
        UserTokenResponseDto userToken = userService.getTempToken(userId);
        return SuccessResponse.created(userToken);
    }

    // 로그인
    @PostMapping("/login")
    ResponseEntity<SuccessResponse<?>> login(@RequestBody LoginRequestDto loginRequest) {
        UserTokenResponseDto userToken = userService.login(loginRequest);
        System.out.println("로그인" + loginRequest); //삭제 필요
        System.out.println("현재 리프레쉬 토큰" + userToken.refreshToken()); //삭제 필요
        return SuccessResponse.ok(userToken);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<?>> logout(@UserId Long userId) {
        userService.logout(userId);
        return SuccessResponse.ok(null);
    }

    // 리프레쉬 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse<?>> reissueToken(@RequestBody UserTokenRequestDto uerTokenRequest) throws JsonProcessingException {
        System.out.println("리프레쉬 토큰 재발급 요청" + uerTokenRequest); //삭제 필요
        UserTokenResponseDto userToken = userService.reissue(uerTokenRequest);
        return SuccessResponse.ok(userToken);
    }

    // 메인홈
    @GetMapping("/main")
    public ResponseEntity<SuccessResponse<?>> getMainPage(@UserId Long userId) {
        MainPageResponseDto mainPage = userService.getMainPage(userId);
        return SuccessResponse.ok(mainPage);
    }


}
