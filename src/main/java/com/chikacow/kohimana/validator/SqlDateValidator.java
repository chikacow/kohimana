package com.chikacow.kohimana.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class SqlDateValidator implements ConstraintValidator<SqlDate, LocalDate> {
    @Override
    public void initialize(SqlDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            context.disableDefaultConstraintViolation(); //turn off default
            context
                    .buildConstraintViolationWithTemplate("Birthdate cannot be emptty")
                    .addConstraintViolation(); //save
            return false;
        }

        if (date.getYear() > 9999) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Birthdate cannot be empty, gonna cause db error if forced")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
