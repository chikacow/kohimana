package com.chikacow.kohimana.validator;

import com.chikacow.kohimana.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<Username, String> {
    private int max;

    @Override
    public void initialize(Username constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.trim().isEmpty()) {
            context.disableDefaultConstraintViolation(); //turn off default
            context
                    .buildConstraintViolationWithTemplate("Username cannot be empty")
                    .addConstraintViolation(); //save
            return false;
        }

        if (username.length() > max) {
            context.disableDefaultConstraintViolation(); //turn off default
            context
                    .buildConstraintViolationWithTemplate("Username is too long")
                    .addConstraintViolation(); //save
            return false;
        }

        if (username.matches(".*\\s.*")) {
            context.disableDefaultConstraintViolation(); // Tắt message mặc định
            context
                    .buildConstraintViolationWithTemplate("Username cannot contain whitespace")
                    .addConstraintViolation(); // Thêm message mới
            return false;
        }

        return true;
    }
}
