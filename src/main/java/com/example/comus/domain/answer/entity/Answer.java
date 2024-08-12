package com.example.comus.domain.answer.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "answer")
@Entity
public class Answer  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}