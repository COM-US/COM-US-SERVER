package com.example.comus.domain.answer.repository;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.question.entity.AnswerType;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.entity.QuestionCategory;
import com.example.comus.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long>{

    @Query("SELECT COUNT(a) FROM Answer a JOIN a.question q WHERE a.user.id = :userId AND q.category = :category")
    long countByUserIdAndCategory(@Param("userId") Long userId, @Param("category") QuestionCategory category);

    @Query("SELECT COUNT(a) FROM Answer a JOIN a.question q WHERE a.user.id = :userId AND q.answerType = :answerType")
    long countByUserIdAndAnswerType(@Param("userId") Long userId, @Param("answerType") AnswerType answerType);

    @Query("SELECT COUNT(a) FROM Answer a WHERE a.user = :user AND a.question = :question")
    int countByUserAndQuestion(@Param("user") User user, @Param("question") Question question);

    Long countByUserIdAndQuestionCategoryAndIsUsedFalse(Long userId, QuestionCategory category);
    List<Answer> findByUserIdAndQuestionCategoryAndIsUsedFalseOrderByCreatedAtAsc(Long userId, QuestionCategory questionCategory);
    List<Answer> findByUserOrderByCreatedAtAsc(User user);
    List<Answer> findByUserAndQuestionOrderByCreatedAtDesc(User user, Question question);

    int countByUserId(Long userId);

    List<Answer> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Answer> findByQuestion(Question question);
}
