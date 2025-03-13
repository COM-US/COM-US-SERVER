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
        int HobbyPercent

) {
    public static CategoryResponseDto from(int dailyCount, int dailyTotalCount, int dailyPercent, int schoolCount, int schoolTotalCount, int schoolPercent, int friendCount, int friendTotalCount, int friendPercent, int familyCount, int familyTotalCount, int familyPercent, int hobbyCount, int hobbyTotalCount, int hobbyPercent) {
        return CategoryResponseDto.builder()
                .DailyCount(dailyCount)
                .DailyTotalCount(dailyTotalCount)
                .DailyPercent(dailyPercent)
                .SchoolCount(schoolCount)
                .SchoolTotalCount(schoolTotalCount)
                .SchoolPercent(schoolPercent)
                .FriendCount(friendCount)
                .FriendTotalCount(friendTotalCount)
                .FriendPercent(friendPercent)
                .FamilyCount(familyCount)
                .FamilyTotalCount(familyTotalCount)
                .FamilyPercent(familyPercent)
                .HobbyCount(hobbyCount)
                .HobbyTotalCount(hobbyTotalCount)
                .HobbyPercent(hobbyPercent)
                .build();
    }
}
