package com.IlyaTr.movie_catalog.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllFieldsNotEmptyValidator.class)
public @interface AllFieldsNotEmpty {
    String message() default "Все поля должны быть заполнены";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
