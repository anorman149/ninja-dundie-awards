package com.ninjaone.dundie_awards.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<ValidUUID, UUID> {
    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext cxt) {
        try{
            //Try to parse the UUID, if it fails, then it's not a valid UUID
            UUID.fromString(uuid.toString());
        }catch(Exception e){
            return false;
        }

        return true;
    }
}
