package com.example.comus.domain.block.dto.response;

import com.example.comus.domain.block.entity.Block;

public record BlockPlaceResponseDto(
        int row,
        int col
) {
    public static BlockPlaceResponseDto of(Block block) {
        return new BlockPlaceResponseDto(block.getBlockRow(), block.getBlockColumn());
    }
}
