package com.chikacow.kohimana.validator;

import com.chikacow.kohimana.util.enums.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumSubsetValidator implements ConstraintValidator<EnumSubset, String> {

    private Set<String> validEnumNames;

    @Override
    public void initialize(EnumSubset constraintAnnotation) {
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();

        validEnumNames = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return value != null && validEnumNames.contains(value);
    }
}

