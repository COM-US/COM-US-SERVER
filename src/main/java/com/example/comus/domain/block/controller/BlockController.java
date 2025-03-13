package com.example.comus.domain.block.controller;

import com.example.comus.domain.block.dto.response.BlockCountResponseDto;
import com.example.comus.domain.block.service.BlockService;
import com.example.comus.global.common.SuccessResponse;
import com.example.comus.global.config.auth.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/block")
@RestController
public class BlockController {
    private final BlockService blockService;

    //사용 가능한 블럭 개수 조회
    @GetMapping("/available-count")
    public ResponseEntity<?> getBlockCount(@UserId Long userId) {
        BlockCountResponseDto blockCount = blockService.getBlockCount(userId);
        return SuccessResponse.ok(blockCount);
    }


}
