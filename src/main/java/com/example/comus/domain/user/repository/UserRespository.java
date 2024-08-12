package com.example.comus.domain.user.repository;

import com.example.comus.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<User, Long>{
}
