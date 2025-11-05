package com.IlyaTr.movie_catalog.validator.annotation;

import com.IlyaTr.movie_catalog.dto.CreateEditObject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.length() < 8) {
            addConstraintViolation(context, "Пароль должен быть не менее 8 символов");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            addConstraintViolation(context, "Пароль должен содержать хотя бы одну заглавную букву");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            addConstraintViolation(context, "Пароль должен содержать хотя бы одну строчную букву");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            addConstraintViolation(context, "Пароль должен содержать хотя бы одну цифру");
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
