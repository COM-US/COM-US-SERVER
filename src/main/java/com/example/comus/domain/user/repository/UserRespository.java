package com.example.comus.domain.user.repository;

import com.example.comus.domain.user.entity.SocialType;
import com.example.comus.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRespository extends JpaRepository<User, Long>{
    User findByNameAndSocialType(String name, SocialType socialType);
    Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);
}
