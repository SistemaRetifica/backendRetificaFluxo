package com.sistema.fluxo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CepValidator implements ConstraintValidator<ValidCep, String> {

    private static final Pattern PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null || cep.isBlank()) {
            return true;
        }
        return PATTERN.matcher(cep).matches();
    }
}
