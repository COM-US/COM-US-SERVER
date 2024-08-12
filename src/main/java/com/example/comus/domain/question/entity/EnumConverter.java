package com.example.comus.domain.question.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public abstract class EnumConverter<T extends Enum<T> & EnumConverter.KoreanEnum> {

    public interface KoreanEnum {
        String getKorean();
    }

    private final Class<T> enumType;

    @JsonValue
    public String getKoreanValue(T enumValue) {
        return enumValue.getKorean();
    }

    @JsonCreator
    public T fromKorean(String korean) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(e -> e.getKorean().equals(korean))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant with Korean name: " + korean));
    }
}
