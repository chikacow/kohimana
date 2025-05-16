package com.chikacow.kohimana.validator;

import com.chikacow.kohimana.util.enums.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class GenderSubsetValidator implements ConstraintValidator<GenderSubset, String> {
    private Gender[] genders;

    @Override
    public void initialize(GenderSubset constraint) {
        this.genders = constraint.anyOf();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        //return s == null || Arrays.asList(genders).contains(Gender.fromString(s));

        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;


    }


}
