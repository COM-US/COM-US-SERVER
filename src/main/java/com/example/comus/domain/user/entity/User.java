package com.example.comus.domain.user.entity;

import com.example.comus.domain.answer.entity.Answer;
import com.example.comus.domain.question.entity.QuestionLike;
import com.example.comus.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "user")
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    private String name;

    private String imageUrl;

    private LocalTime todayChatTime;

    private LocalTime totalChatTime;

    private int totalChatCount;

    @OneToMany(mappedBy = "user")
    private List<Answer> answers;

    @OneToMany(mappedBy = "user")
    private List<QuestionLike> questionLikes;

    public void resetTodayChatTime() {
        todayChatTime = LocalTime.of(0, 0, 0);
    }

    public List<Answer> getAnswer() {
        return answers;
    }
}