package com.example.comus.domain.block.dto.response;

import com.example.comus.domain.block.entity.Block;

import java.util.List;

public record BlockResponseDto(
        BlockInfoResponseDto blockInfo,
        List<BlockPlaceResponseDto> blockPlace
) {
    public static BlockResponseDto from(Block block, BlockInfoResponseDto blockInfoResponseDto) {
        return new BlockResponseDto(blockInfoResponseDto, List.of(BlockPlaceResponseDto.of(block)));
    }
}
