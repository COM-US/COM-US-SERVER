package com.example.comus.domain.user.controller;

import com.example.comus.domain.answer.dto.response.StatisticResponseDto;
import com.example.comus.domain.answer.service.AnswerService;
import com.example.comus.domain.user.dto.request.LoginRequestDto;
import com.example.comus.domain.user.dto.request.UserTokenRequestDto;
import com.example.comus.domain.user.dto.response.*;
import com.example.comus.domain.user.service.UserService;
import com.example.comus.global.common.SuccessResponse;
import com.example.comus.global.config.auth.UserId;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        UserTokenResponseDto userToken = userService.reissue(uerTokenRequest);
        return SuccessResponse.ok(userToken);
    }

    @GetMapping("/mypage")
    public ResponseEntity<SuccessResponse<?>> getUserInfoAndStatic(@UserId Long userId) {
        StatisticResponseDto answerStatistic = answerService.getAnswerStatistic(userId);
        UserInfoResponseDto userInfo = userService.getUserInfo(userId);
        MyPageResponseDto myPageResponseDto = new MyPageResponseDto(userInfo, answerStatistic);
        return SuccessResponse.ok(myPageResponseDto);

    }

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getUserInfoAndBlock(@UserId Long userId) {
        MainPageUserresponseDto userInfo = userService.getMainPageUserInfo(userId);
        List<BlockResponseDto> block = userService.getBlockList(userId);
        CategoryResponseDto category = userService.getCategory(userId);
        MainPageResponseDto mainPageResponseDto = new MainPageResponseDto(userInfo, category, block);
        return SuccessResponse.ok(mainPageResponseDto);
    }


}
