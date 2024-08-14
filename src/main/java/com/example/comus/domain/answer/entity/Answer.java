package com.example.comus.domain.answer.entity;

import com.example.comus.domain.block.entity.Block;
import com.example.comus.domain.question.entity.Question;
import com.example.comus.domain.signlanguage.entity.SignLanguage;
import com.example.comus.domain.user.entity.User;
import com.example.comus.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Table(name = "answer")
@Entity
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answerContent;

    @OneToMany
    private List<Block> blocks;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}