package com.chikacow.kohimana.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Password must include letter a-z, number and special character, at least 8 in length";
    int min() default 8;
    int max() default 20;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
