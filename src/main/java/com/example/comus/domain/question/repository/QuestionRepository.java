package com.example.comus.domain.question.repository;

import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.entity.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCategory(QuestionCategory category);


    int countByCategory(QuestionCategory category);
}
