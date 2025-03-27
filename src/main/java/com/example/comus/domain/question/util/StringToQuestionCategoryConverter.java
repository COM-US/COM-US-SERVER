package com.example.comus.domain.question.util;

import com.example.comus.domain.question.entity.QuestionCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToQuestionCategoryConverter implements Converter<String, QuestionCategory> {
    @Override
    public QuestionCategory convert(String source) {
        return QuestionCategory.fromString(source);
    }
}
