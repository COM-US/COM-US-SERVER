package com.example.comus.domain.question.entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSortTypeConverter implements Converter<String, SortType> {
    @Override
    public SortType convert(String source) {return SortType.fromString(source);}
}
