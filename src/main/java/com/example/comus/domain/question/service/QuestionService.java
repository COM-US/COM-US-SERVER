package com.example.comus.domain.question.service;

import com.example.comus.domain.question.dto.response.QuestionResponseDto;
import com.example.comus.domain.question.entity.Category;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

}
