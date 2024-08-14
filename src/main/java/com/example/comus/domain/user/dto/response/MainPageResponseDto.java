package com.example.comus.domain.user.dto.response;

import java.util.List;

public record MainPageResponseDto(
        MainPageUserresponseDto user,
        CategoryResponseDto category,
        List<BlockResponseDto> block
) {
}
