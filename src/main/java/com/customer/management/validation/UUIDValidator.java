package com.customer.management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<ValidUUID, UUID> {
    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        return value != null; // Add additional UUID format checks if needed
    }
}
