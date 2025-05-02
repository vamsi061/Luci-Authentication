package com.ai.validator;

import com.ai.util.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null) {
            addMessage(context, "Password must not be null");
            return false;
        }

        boolean valid = true;

        if (password.length() < 8) {
            addMessage(context, "Password must be at least 8 characters long");
            valid = false;
        }
        if (!password.matches(".*[A-Z].*")) {
            addMessage(context, "Password must contain at least one uppercase letter");
            valid = false;
        }
        if (!password.matches(".*[a-z].*")) {
            addMessage(context, "Password must contain at least one lowercase letter");
            valid = false;
        }
        if (!password.matches(".*\\d.*")) {
            addMessage(context, "Password must contain at least one digit");
            valid = false;
        }
        if (!password.matches(".*[@$!%*?&].*")) {
            addMessage(context, "Password must contain at least one special character (@$!%*?&)");
            valid = false;
        }

        return valid;
    }

    private void addMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
