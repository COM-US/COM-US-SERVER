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

    List<Block> findByAnswer(Answer answer);

    // block -> answer ->userId로 level이 가장 높은 Block 조회
    @Query("SELECT b FROM Block b WHERE b.level = (SELECT MAX(b2.level) FROM Block b2 WHERE b2.answer.user.id = :userId) AND b.answer.user.id = :userId")
    List<Block> findMaxLevelBlocksByUserId(Long userId);

}