package com.example.comus.domain.block.entity;

import com.example.comus.domain.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "block")
@Entity
public class Block  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String level;

    private int row;

    private int col;

    @OneToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;



}