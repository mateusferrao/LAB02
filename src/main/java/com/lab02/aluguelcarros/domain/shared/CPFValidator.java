package com.lab02.aluguelcarros.domain.shared;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<ValidCPF, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true; // deixar @NotBlank tratar nulls
        return CPF.isValid(value);
    }
}
