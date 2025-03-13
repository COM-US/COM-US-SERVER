package com.example.comus.domain.block.entity;

import com.example.comus.domain.question.entity.QuestionCategory;
import com.example.comus.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@Table(name = "block_count")
@Entity
public class BlockCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    int DailyCount;
    int SchoolCount;
    int FriendCount;
    int FamilyCount;
    int HobbyCount;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    void addBlockCount(QuestionCategory category){
        switch(category){
            case DAILY:
                DailyCount++; break;
            case SCHOOL:
                SchoolCount++; break;
            case FRIEND:
                FriendCount++; break;
            case FAMILY:
                FamilyCount++; break;
            case HOBBY:
                HobbyCount++; break;
        }
    }

}
