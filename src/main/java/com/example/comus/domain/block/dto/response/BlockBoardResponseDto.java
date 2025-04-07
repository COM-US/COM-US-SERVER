package com.example.comus.domain.block.dto.response;

import java.util.List;

public record BlockBoardResponseDto(
        int level,
        List<BlockResponseDto> blocks
) {
    public static BlockBoardResponseDto of(int level, List<BlockResponseDto> blockList) {
        return new BlockBoardResponseDto(level, blockList);
    }
}
