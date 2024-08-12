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

    private String signLanguageImageUrl;

    private String signLanguageVideoUrl;

    private String signLanguageDescription;


}