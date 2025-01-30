package com.example.comus.domain.question.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public abstract class EnumConverter {

    public interface KoreanEnum {
        String getKorean();
    }

    @JsonCreator
    public static <E extends Enum<E> & KoreanEnum> E fromKorean(Class<E> enumType, String korean) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(e -> e.getKorean().equals(korean))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant with Korean name: " + korean));
    }
}
