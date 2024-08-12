package com.example.comus.domain.block.repository;

import com.example.comus.domain.block.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, Long>{
}
