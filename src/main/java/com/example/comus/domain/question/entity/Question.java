package com.example.comus.domain.question.entity;

import com.example.comus.domain.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "question")
@Entity
public class Question  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String QuestionContent;

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;
}