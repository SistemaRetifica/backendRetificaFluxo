package com.sistema.fluxo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class TelefoneValidator implements ConstraintValidator<ValidTelefone, String> {

    // Aceita: (11) 98765-4321 ou (11) 3456-7890
    private static final Pattern PATTERN = Pattern.compile(
        "^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$"
    );

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null || telefone.isBlank()) {
            return true;
        }
        return PATTERN.matcher(telefone).matches();
    }
}
