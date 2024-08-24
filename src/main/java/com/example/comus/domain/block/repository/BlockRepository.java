package com.example.comus.domain.block.repository;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.block.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {

    Optional<Block> findTopByAnswerIdOrderByCreatedAtDesc(Long answerId);

    List<Block> findByAnswer(Answer answer);
    Optional<Block> findByAnswerId(Long answerId);

    List<Block> findByAnswerIn(List<Answer> answers);
}