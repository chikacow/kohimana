package com.chikacow.kohimana.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {
    String message() default "Invalid username";
    int max() default 20;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}