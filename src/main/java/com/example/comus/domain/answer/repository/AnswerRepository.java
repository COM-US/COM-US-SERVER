package com.example.comus.domain.answer.repository;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long>{

    @Query("SELECT COUNT(a) FROM Answer a JOIN a.question q WHERE a.user.id = :userId AND q.category = :category")
    long countByUserIdAndCategory(@Param("userId") Long userId, @Param("category") Category category);

    @Query("SELECT COUNT(a) FROM Answer a JOIN a.question q WHERE a.user.id = :userId AND q.answerType = :answerType")
    long countByUserIdAndAnswerType(@Param("userId") Long userId, @Param("answerType") AnswerType answerType);


    long countByUserId(Long userId);

}
