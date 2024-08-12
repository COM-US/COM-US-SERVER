package com.example.comus.domain.answer.service;

import com.example.comus.domain.answer.repository.AnswerRepository;
import com.example.comus.domain.answer.dto.response.StatisticResponseDto;
import com.example.comus.domain.question.entity.Category;
import com.example.comus.domain.question.entity.AnswerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public StatisticResponseDto getAnswerStatistic(Long userId) {

        long totalAnswers = answerRepository.countByUserId(userId);
        if (totalAnswers == 0) {
            return new StatisticResponseDto(0, 0, 0, 0, 0, 0, 0, 0);
        }

        double sentenceRatio = getRatio("SENTENCE", totalAnswers, "answerType", userId);
        double multipleChoiceRatio = getRatio("MULTIPLE_CHOICE", totalAnswers, "answerType" , userId);
        double dailyQuestionRatio = getRatio("DAILY", totalAnswers, "category" , userId);
        double schoolQuestionRatio = getRatio("SCHOOL", totalAnswers, "category" , userId);
        double hobbyQuestionRatio = getRatio("FRIEND", totalAnswers, "category" , userId);
        double familyQuestionRatio = getRatio("FAMILY", totalAnswers, "category" , userId);
        double friendQuestionRatio = getRatio("HOBBY", totalAnswers, "category" , userId);
        double randomQuestionRatio = getRatio("RANDOM", totalAnswers, "category" , userId);

        return new StatisticResponseDto(
                sentenceRatio,
                multipleChoiceRatio,
                dailyQuestionRatio,
                schoolQuestionRatio,
                hobbyQuestionRatio,
                familyQuestionRatio,
                friendQuestionRatio,
                randomQuestionRatio
        );
    }

    private double getRatio(String input, long totalAnswerCnt, String type, Long userId) {
        long count = switch (type) {
            case "category" -> answerRepository.countByUserIdAndCategory(userId, Category.valueOf(input));
            case "answerType" -> answerRepository.countByUserIdAndAnswerType(userId, AnswerType.valueOf(input));
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
        return calculatePercentage(count, totalAnswerCnt);
    }

    private double calculatePercentage(long count, long total) {
        double result = total == 0 ? 0 : (double) count / total * 100;
        return Math.round(result * 10) / 10.0;
    }


}
