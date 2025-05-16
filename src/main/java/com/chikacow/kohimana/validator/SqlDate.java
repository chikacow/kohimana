package com.chikacow.kohimana.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SqlDateValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlDate {
    String message() default "This date cause error when saving to SQL";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}