package com.example.comus.domain.user.controller;

import com.example.comus.domain.user.dto.response.UserTokenResponseDto;
import com.example.comus.domain.user.service.UserService;
import com.example.comus.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;


    // 임시 발급 API 입니다. 추후 로그인 기능이 완성되면 삭제할 예정입니다
    @PostMapping("/token/{userId}")
    public ResponseEntity<SuccessResponse<?>> getToken(@PathVariable(name = "userId") Long userId) {
        final UserTokenResponseDto userTokenResponseDto = userService.getToken(userId);
        return SuccessResponse.created(userTokenResponseDto);
    }


}
