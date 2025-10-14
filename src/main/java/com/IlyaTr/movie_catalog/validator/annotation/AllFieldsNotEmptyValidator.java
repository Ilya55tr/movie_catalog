package com.IlyaTr.movie_catalog.validator.annotation;

import com.IlyaTr.movie_catalog.dto.CreateEditObject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AllFieldsNotEmptyValidator implements ConstraintValidator<AllFieldsNotEmpty, CreateEditObject> {
    @Override
    public boolean isValid(CreateEditObject createEditObject,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (createEditObject == null){
            return true;
        }
        return Arrays.stream(createEditObject.getClass().getDeclaredFields())
                .allMatch(field -> {
                    field.setAccessible(true);
                    if (field.getName().equals("image")){
                        return true;
                    }
                    try {
                        Object value = field.get(createEditObject);
                        if (value == null){
                            return false;
                        }
                        if (value instanceof String s && s.trim().isEmpty() ){
                         return false;
                        }
                        return true;
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                });
    }
}
