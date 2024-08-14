package com.example.comus.domain.signlanguage.repository;

import com.example.comus.domain.signlanguage.entity.SignLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignLanguageRepository extends JpaRepository<SignLanguage, Long> {
    List<SignLanguage> findBySignLanguageName(String word);
}
