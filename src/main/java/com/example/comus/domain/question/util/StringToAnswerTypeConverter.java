package com.example.comus.domain.question.util;

import com.example.comus.domain.question.entity.AnswerType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAnswerTypeConverter implements Converter<String, AnswerType> {
    @Override
    public AnswerType convert(String source) {
        return AnswerType.fromString(source);
    }
}
