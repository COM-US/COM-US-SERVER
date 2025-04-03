package com.example.comus.domain.block.repository;

import com.example.comus.domain.block.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {

    // block -> answer ->userId로 level이 가장 높은 Block 조회
    @Query("SELECT b FROM Block b WHERE b.level = (SELECT MAX(b2.level) FROM Block b2 WHERE b2.answer.user.id = :userId) AND b.answer.user.id = :userId")
    List<Block> findMaxLevelBlocksByUserId(Long userId);

    @Query("SELECT COALESCE(MAX(b.level), 1) FROM Block b WHERE b.answer.user.id = :userId")
    int findMaxLevelByUserId(Long userId);

    int countByAnswer_UserIdAndLevel(Long userId, int level);

    boolean existsByBlockRowAndBlockColumnAndLevelAndAnswer_UserId(int row, int column, int currentLevel, Long answerId);
}