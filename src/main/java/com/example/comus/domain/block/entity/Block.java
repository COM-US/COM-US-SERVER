package com.example.comus.domain.block.entity;

import com.example.comus.domain.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Table(name = "block")
@Entity
public class Block  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int level;

    private int blockRow;

    private int blockColumn;

    @OneToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;


}