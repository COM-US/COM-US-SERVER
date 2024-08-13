package com.example.comus.domain.signlanguage.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sign_language")
@Entity
public class SignLanguage{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String signLanguageName;

    //임시
    @Column(columnDefinition = "varchar(255) default 'https://github.com/user-attachments/assets/2ddd219f-1f38-4524-97ae-43296fda33b8'")
    private String signLanguageImageUrl;

    private String signLanguageVideoUrl;

    private String signLanguageDescription;


}