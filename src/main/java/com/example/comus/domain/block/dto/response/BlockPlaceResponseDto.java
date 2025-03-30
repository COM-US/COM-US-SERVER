package com.example.comus.domain.block.dto.response;

import com.example.comus.domain.block.entity.Block;

public record BlockPlaceResponseDto(
        Long blockId,
        int row,
        int col
) {
    public static BlockPlaceResponseDto of(Block block) {
        return new BlockPlaceResponseDto(
                block.getId(),
                block.getBlockRow(),
                block.getBlockColumn()
        );
    }
}
