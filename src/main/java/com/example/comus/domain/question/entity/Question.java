package com.example.comus.domain.question.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "question")
@Entity
public class Question  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}