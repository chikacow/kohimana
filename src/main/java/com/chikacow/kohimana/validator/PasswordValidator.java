package com.chikacow.kohimana.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private int max;

    @Override
    public void initialize(Password constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) {
            context.disableDefaultConstraintViolation(); //turn off default
            context
                    .buildConstraintViolationWithTemplate("Password must not be blank")
                    .addConstraintViolation(); //save
            return false;
        }
        if (s.length() > this.max) {
            context.disableDefaultConstraintViolation(); //turn off default
            context
                    .buildConstraintViolationWithTemplate("Password is too long")
                    .addConstraintViolation(); //save
            return false;
        }

        // Regex: ít nhất 1 chữ cái, 1 số, 1 ký tự đặc biệt
        String pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$";

        return s.matches(pattern);
    }

}
