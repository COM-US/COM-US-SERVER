package com.example.comus.domain.answer.service;

import com.example.comus.domain.answer.dto.request.AnswerRequestDto;
import com.example.comus.domain.answer.dto.response.*;
import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.answer.repository.AnswerRepository;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.repository.QuestionRepository;
import com.example.comus.domain.answer.dto.response.WeeklyAnswerResponseDto;
import com.example.comus.domain.user.entity.User;
import com.example.comus.domain.user.repository.UserRespository;
import com.example.comus.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    // 주간 답변 기록 조회
    public List<WeeklyAnswerResponseDto> getWeeklyAnswers(Long userId) {
        LocalDateTime[] weekRange = getCurrentWeekRange();
        LocalDateTime startDate = weekRange[0];
        LocalDateTime endDate = weekRange[1];

        List<Answer> answers = answerRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate);
        Map<DayOfWeek, WeeklyAnswerResponseDto> weeklyAnswerMap = new HashMap<>();

        for (Answer answer : answers) {
            DayOfWeek dayOfWeek = answer.getCreatedAt().getDayOfWeek();
            weeklyAnswerMap.putIfAbsent(dayOfWeek,
                    new WeeklyAnswerResponseDto(answer.getCreatedAt().format(DateTimeFormatter.ofPattern("E", Locale.KOREAN)),
                            answer.getQuestion().getCategory()));
        }
        return new ArrayList<>(weeklyAnswerMap.values());
    }

    // 이번 주
    private static LocalDateTime[] getCurrentWeekRange() {
        LocalDate now = LocalDate.now();
        return new LocalDateTime[]{
                now.with(DayOfWeek.MONDAY).atStartOfDay(),
                now.with(DayOfWeek.SUNDAY).atTime(23, 59, 59)
        };
    }
}

