package com.example.comus.domain.block.entity;


import com.example.comus.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
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

    public void addDailyBlockCount() {DailyCount++;}
    public void addSchoolBlockCount() {SchoolCount++;}
    public void addFriendBlockCount() {FriendCount++;}
    public void addFamilyBlockCount() {FamilyCount++;}
    public void addHobbyBlockCount() {HobbyCount++;}

}
