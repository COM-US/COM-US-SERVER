package com.example.comus.domain.question.service;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.answer.repository.AnswerRepository;
import com.example.comus.domain.answer.service.AnswerService;
import com.example.comus.domain.question.dto.response.QuestionListResponseDto;
import com.example.comus.domain.question.dto.response.QuestionResponseDto;
import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.Category;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.repository.QuestionRepository;
import com.example.comus.domain.user.entity.User;
import com.example.comus.domain.user.repository.UserRespository;
import com.example.comus.global.error.exception.EntityNotFoundException;
import com.example.comus.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.comus.global.error.ErrorCode.QUESTION_NOT_FOUND;
import static com.example.comus.global.error.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRespository userRepository;
    private final AnswerRepository answerRepository;

    public List<QuestionListResponseDto> getQuestions(Category category, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        List<Question> questions = questionRepository.findByCategory(category);

        return questions.stream()
                .map(question -> toQuestionListResponseDto(question, user))
                .collect(Collectors.toList());
    }

    public List<QuestionListResponseDto> getAllQuestions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        List<Question> questions = questionRepository.findAll();

        return questions.stream()
                .map(question -> toQuestionListResponseDto(question, user))
                .collect(Collectors.toList());
    }

    private QuestionListResponseDto toQuestionListResponseDto(Question question, User user) {
        int questionCount = answerRepository.countByUserAndQuestion(user, question);

        return new QuestionListResponseDto(
                question.getId(),
                question.getCategory(),
                question.getAnswerType(),
                question.getQuestionContent(),
                questionCount
        );
    }

    public QuestionResponseDto getQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new EntityNotFoundException(QUESTION_NOT_FOUND));
        return new QuestionResponseDto(
                question.getId(),
                LocalDate.now(),
                question.getCategory(),
                question.getAnswerType(),
                question.getQuestionContent()
        );
    }
    public List<String> getMultipleChoiceAnswer(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new EntityNotFoundException(QUESTION_NOT_FOUND));
        String multipleChoices = question.getMultipleChoices();
        if (multipleChoices == null) {
            return null;
        }
        return List.of(multipleChoices.split(","))
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());

    }

    public QuestionListResponseDto getQuestionAndCount(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new EntityNotFoundException(QUESTION_NOT_FOUND));
        int questionCount = answerRepository.countByQuestionId(questionId);
        return new QuestionListResponseDto(
                question.getId(),
                question.getCategory(),
                question.getAnswerType(),
                question.getQuestionContent(),
                questionCount
        );
    }
}
