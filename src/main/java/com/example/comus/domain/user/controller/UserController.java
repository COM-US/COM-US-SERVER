package com.example.comus.domain.user.controller;

import com.example.comus.domain.answer.dto.response.StatisticResponseDto;
import com.example.comus.domain.answer.service.AnswerService;
import com.example.comus.domain.user.dto.response.*;
import com.example.comus.domain.user.service.UserService;
import com.example.comus.global.common.SuccessResponse;
import com.example.comus.global.config.auth.UserId;
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

    // 임시 발급 API 입니다. 추후 로그인 기능이 완성되면 삭제할 예정입니다
    @PostMapping("/token/{userId}")
    public ResponseEntity<SuccessResponse<?>> getToken(@PathVariable(name = "userId") Long userId) {
        final UserTokenResponseDto userTokenResponseDto = userService.getToken(userId);
        return SuccessResponse.created(userTokenResponseDto);
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
