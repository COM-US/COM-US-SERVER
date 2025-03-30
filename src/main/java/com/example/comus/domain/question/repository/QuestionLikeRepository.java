package com.example.comus.domain.question.repository;

import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.question.entity.QuestionLike;
import com.example.comus.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionLikeRepository extends JpaRepository<QuestionLike, Long> {
    boolean existsByUserAndQuestion(User user, Question question);

    Optional<QuestionLike> findByUserAndQuestion(User user, Question question);

    int countByUserId(Long userId);
}
