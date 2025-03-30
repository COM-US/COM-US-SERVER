package com.example.comus.domain.answer.service;

import com.example.comus.domain.answer.dto.request.AnswerRequestDto;
import com.example.comus.domain.answer.dto.response.*;
import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.answer.repository.AnswerRepository;
import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.entity.QuestionCategory;
import com.example.comus.domain.question.repository.QuestionRepository;
import com.example.comus.domain.user.entity.User;
import com.example.comus.domain.user.repository.UserRespository;
import com.example.comus.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.example.comus.global.error.ErrorCode.QUESTION_NOT_FOUND;
import static com.example.comus.global.error.ErrorCode.USER_NOT_FOUND;

@Transactional
@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final UserRespository userRepository;
    private final QuestionRepository questionRepository;

    public StatisticResponseDto getAnswerStatistic(Long userId) {

        long totalAnswers = answerRepository.countByUserId(userId);
        if (totalAnswers == 0) {
            return new StatisticResponseDto(0, 0, 0, 0, 0, 0, 0);
        }

        double sentenceRatio = getRatio("SENTENCE", totalAnswers, "answerType", userId);
        double multipleChoiceRatio = getRatio("MULTIPLE_CHOICE", totalAnswers, "answerType", userId);
        double dailyQuestionRatio = getRatio("DAILY", totalAnswers, "category", userId);
        double schoolQuestionRatio = getRatio("SCHOOL", totalAnswers, "category", userId);
        double hobbyQuestionRatio = getRatio("FRIEND", totalAnswers, "category", userId);
        double familyQuestionRatio = getRatio("FAMILY", totalAnswers, "category", userId);
        double friendQuestionRatio = getRatio("HOBBY", totalAnswers, "category", userId);

        return new StatisticResponseDto(
                sentenceRatio,
                multipleChoiceRatio,
                dailyQuestionRatio,
                schoolQuestionRatio,
                hobbyQuestionRatio,
                familyQuestionRatio,
                friendQuestionRatio
        );
    }

    private double getRatio(String input, long totalAnswerCnt, String type, Long userId) {
        long count = switch (type) {
            case "category" -> answerRepository.countByUserIdAndCategory(userId, QuestionCategory.valueOf(input));
            case "answerType" -> answerRepository.countByUserIdAndAnswerType(userId, AnswerType.valueOf(input));
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
        return calculatePercentage(count, totalAnswerCnt);
    }

    private double calculatePercentage(long count, long total) {
        double result = total == 0 ? 0 : (double) count / total * 100;
        return Math.round(result * 10) / 10.0;
    }

    // 답변하기
    @Transactional
    public void createAnswer(Long userId, AnswerRequestDto answerRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        Question question = questionRepository.findById(answerRequest.questionId())
                .orElseThrow(() -> new EntityNotFoundException(QUESTION_NOT_FOUND));

        Answer answer = answerRequest.toEntity(user, question);

        answerRepository.save(answer);
    }

    // 이전 답변 보기
    public List<AnswerHistoryListResponseDto> getAnswerHistory(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        List<Answer> answers = answerRepository.findByUserOrderByCreatedAtAsc(user);

        // 날짜별로 그룹화
        return answers.stream()
                .collect(Collectors.groupingBy(this::getAnswerDate))
                .entrySet().stream()
                .map(entry -> new AnswerHistoryListResponseDto(entry.getKey(),
                        entry.getValue().stream()
                                .map(AnswerHistoryResponseDto::from)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    private String getAnswerDate(Answer answer) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREAN);
        return answer.getCreatedAt().format(dateFormatter);
    }

    //이전 답변 상세보기
    public AnswerByQuestionResponseDto getAnswerByQuestion(Long userId, Long questionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException(QUESTION_NOT_FOUND));

        List<AnswerResponseDto> answers = answerRepository.findByUserAndQuestionOrderByCreatedAtDesc(user, question).stream()
                .map(AnswerResponseDto::from)
                .collect(Collectors.toList());

        return AnswerByQuestionResponseDto.from(question,answers);
    }
}

