package com.example.comus.domain.block.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "block")
@Entity
public class Block  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}