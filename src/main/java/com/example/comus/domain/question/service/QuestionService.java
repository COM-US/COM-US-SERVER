package com.example.comus.domain.question.service;

import com.example.comus.domain.question.dto.response.QuestionResponseDto;
import com.example.comus.domain.question.entity.Category;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.repository.QuestionRepository;
import com.example.comus.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.comus.global.error.ErrorCode.QUESTION_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;
    public List<QuestionResponseDto> getQuestions(Category category) {
        return questionRepository.findByCategory(category);
    }

    public List<QuestionResponseDto> getAllQuestions() {
        return questionRepository.findAllBy();
    }

    public List<String> getMultipleChoiceAnswer(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new EntityNotFoundException(QUESTION_NOT_FOUND));
        String multipleChoices = question.getMultipleChoices();
        return List.of(multipleChoices.split(","))
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());

    }
}
