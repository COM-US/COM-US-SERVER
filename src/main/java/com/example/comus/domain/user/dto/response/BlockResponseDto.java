package com.example.comus.domain.user.dto.response;

import com.example.comus.domain.block.entity.Block;
import com.example.comus.domain.question.entity.QuestionCategory;
import lombok.Builder;

@Builder
public record BlockResponseDto(
        Long id,
        int level,
        QuestionCategory category,
        int blockRow,
        int blockColumn
) {
    public static BlockResponseDto fromEntity(Block block) {
        return BlockResponseDto.builder()
                .id(block.getId())
                .level(block.getLevel())
                .category(block.getAnswer().getQuestion().getCategory())
                .blockRow(block.getBlockRow())
                .blockColumn(block.getBlockColumn())
                .build();
    }

}
