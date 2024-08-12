package com.example.comus.domain.question.repository;

import com.example.comus.domain.question.dto.response.QuestionResponseDto;
import com.example.comus.domain.question.entity.Category;
import com.example.comus.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>{

    List<QuestionResponseDto> findByCategory(Category category);
    List<QuestionResponseDto> findAllBy();

}
