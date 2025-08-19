package com.likelionhgu.nvidia.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Converter
public class IntegerSetConverter implements AttributeConverter<Set<Integer>, String> {
    @Override
    public String convertToDatabaseColumn(Set<Integer> attribute) {
        if (attribute == null || attribute.isEmpty()) return "";
        return attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return new TreeSet<>();
        return Arrays.stream(dbData.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}