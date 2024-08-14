package com.example.comus.domain.user.dto.response;

import lombok.Builder;

@Builder
public record CategoryResponseDto(
        int DailyCount,
        int DailyTotalCount,
        int DailyPercent,

        int SchoolCount,
        int SchoolTotalCount,
        int SchoolPercent,

        int FriendCount,
        int FriendTotalCount,
        int FriendPercent,

        int FamilyCount,
        int FamilyTotalCount,
        int FamilyPercent,

        int HobbyCount,
        int HobbyTotalCount,
        int HobbyPercent,

        int RandomCount,
        int RandomTotalCount,
        int RandomPercent
) {
}
