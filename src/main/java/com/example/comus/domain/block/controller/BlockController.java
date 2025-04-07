package com.example.comus.domain.block.controller;

import com.example.comus.domain.block.dto.request.BlockRequestDto;
import com.example.comus.domain.block.dto.response.BlockBoardResponseDto;
import com.example.comus.domain.block.dto.response.BlockCountResponseDto;
import com.example.comus.domain.block.service.BlockService;
import com.example.comus.global.common.SuccessResponse;
import com.example.comus.global.config.auth.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    //블럭 배치
    @PostMapping
    public ResponseEntity<?> createBlockBatch(@UserId Long userId, @RequestBody BlockRequestDto blockRequest) {
        blockService.createBlock(userId, blockRequest);
        return SuccessResponse.ok(null);
    }

    //블럭 조회
    @GetMapping
    public ResponseEntity<?> getBlock(@UserId Long userId)  {
        BlockBoardResponseDto blockBoard = blockService.getBlock(userId);
        return SuccessResponse.ok(blockBoard);
    }

    //블록 삭제(관리자용)
    @DeleteMapping
    public ResponseEntity<?> deleteBlock(@UserId Long userId) {
        blockService.deleteBlock(userId);
        return SuccessResponse.ok(null);
    }
}
