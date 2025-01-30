package com.example.comus.domain.question.entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAnswerTypeConverter implements Converter<String, AnswerType> {
    @Override
    public AnswerType convert(String source) {
        return AnswerType.fromString(source);
    }
}
