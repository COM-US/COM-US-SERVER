package com.example.comus.domain.question.entity;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.signlanguage.entity.SignLanguage;
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
    private QuestionCategory category;

    private String questionContent;

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    @OneToMany(mappedBy = "question")
    private List<QuestionLike> questionLikes;

    private String multipleChoices;


}