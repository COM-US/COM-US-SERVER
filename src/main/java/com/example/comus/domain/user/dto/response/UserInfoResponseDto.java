package com.example.comus.domain.user.dto.response;

import com.example.comus.domain.answer.dto.response.WeeklyAnswerResponseDto;
import com.example.comus.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

@Builder
public record UserInfoResponseDto(
        String name,
        String imageUrl,
        int answerCount,
        int likeCount,
        String week,
        List<WeeklyAnswerResponseDto> weeklyAnswer
) {
    private static String getCurrentWeek() {
        LocalDate now = LocalDate.now();
        int weekOfMonth = now.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
        return now.format(DateTimeFormatter.ofPattern("MM월", Locale.KOREAN)) + " " + weekOfMonth + "주차";
    }

    public static UserInfoResponseDto of(User user, int answerCount, int likeCount, List<WeeklyAnswerResponseDto> weeklyAnswer) {
        return UserInfoResponseDto.builder()
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .answerCount(answerCount)
                .likeCount(likeCount)
                .week(getCurrentWeek())
                .weeklyAnswer(weeklyAnswer)
                .build();
    }
}
