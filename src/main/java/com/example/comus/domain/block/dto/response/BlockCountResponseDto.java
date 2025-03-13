package com.example.comus.domain.block.dto.response;

public record BlockCountResponseDto(
        int dailyBlockCount,
        int schoolBlockCount,
        int hobbyBlockCount,
        int familyBlockCount,
        int friendBlockCount
) {
    public static BlockCountResponseDto of(int dailyBlockCount, int schoolBlockCount, int hobbyBlockCount, int familyBlockCount, int friendBlockCount) {
        return new BlockCountResponseDto(dailyBlockCount, schoolBlockCount, hobbyBlockCount, familyBlockCount, friendBlockCount);
    }
}
