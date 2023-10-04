package com.heavens.stream.helpers;

import com.heavens.stream.enums.CodeType;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.beans.FeatureDescriptor;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Helper {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String CHARACTERS_ONLY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS_ONLY = "0123456789";
    private static final String NUMBERS_CHARACTER_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NUMBERS_CHARACTER_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String CHARACTERS_UPPER_CASE_ONLY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CHARACTERS_LOWER_CASE_ONLY = "abcdefghijklmnopqrstuvwxyz";
    private static final int STRING_LENGTH_MIN = 4;
    private static final int STRING_LENGTH_MAX = 10;
    private static final SecureRandom random = new SecureRandom();

    public static  <T, R> Page<R> convertPageToDto(Page<T> page, Function<T, R> converter) {
        List<R> dtos = page.getContent().stream()
                .map(converter)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    }

    public static String generateRandomCode(int length, CodeType type) {
        length = Math.min(length, STRING_LENGTH_MAX);
        length = Math.max(length, STRING_LENGTH_MIN);
        String characters = getCharactersBasedOnType(type);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    private static String getCharactersBasedOnType(CodeType type) {
        return switch (type) {
            case NUMBERS_CHARACTER_LOWER_CASE -> NUMBERS_CHARACTER_LOWER_CASE;
            case CHARACTERS_ONLY -> CHARACTERS_ONLY;
            case CHARACTERS -> CHARACTERS;
            case NUMBERS_CHARACTER_UPPER_CASE -> NUMBERS_CHARACTER_UPPER_CASE;
            case CHARACTERS_UPPER_CASE_ONLY -> CHARACTERS_UPPER_CASE_ONLY;
            case CHARACTERS_LOWER_CASE_ONLY -> CHARACTERS_LOWER_CASE_ONLY;
            default -> NUMBERS_ONLY;
        };
    }

    public static String randomUUIDStringID(){
        return UUID.randomUUID().toString();
    }

    // Generic method to get the names of null properties in the given object
    public static <T> String[] getNullPropertyNames(T object) {
        final BeanWrapper src = new BeanWrapperImpl(object);
        return Stream.of(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> src.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}
