package com.example.comus.domain.block.repository;

import com.example.comus.domain.block.entity.BlockCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockCountRepository extends JpaRepository<BlockCount, Long> {
    BlockCount findByUserId(Long userId);
}
