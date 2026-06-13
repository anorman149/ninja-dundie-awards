package com.ninjaone.dundie_awards.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UUIDValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface ValidUUID {
    String message() default "Provided UUID is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
