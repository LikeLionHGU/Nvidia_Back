package com.likelionhgu.nvidia.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.TreeSet;

@Converter
public class IntegerSetConverter implements AttributeConverter<Set<Integer>, String> {

    // Set<Integer>를 데이터베이스에 저장할 String으로 변환
    @Override
    public String convertToDatabaseColumn(Set<Integer> attribute) {
        if (attribute == null) {
            return null;
        }
        // 예: [1, 2, 3] -> "1,2,3"
        return attribute.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    // 데이터베이스의 String을 Set<Integer>로 변환
    @Override
    public Set<Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new TreeSet<>();
        }
        // 예: "1,2,3" -> [1, 2, 3]
        return Arrays.stream(dbData.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}