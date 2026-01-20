package com.sistema.fluxo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isBlank()) {
            return true; // Use @NotBlank separadamente
        }
        
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");
        
        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        
        // Verifica CPFs inválidos conhecidos
        if (cpf.matches("(\\d)\\1{10}")) {
            return false; // 000.000.000-00, 111.111.111-11, etc.
        }
        
        // Valida dígitos verificadores
        return validarDigitoVerificador(cpf, 9) && validarDigitoVerificador(cpf, 10);
    }
    
    private boolean validarDigitoVerificador(String cpf, int posicao) {
        int soma = 0;
        int peso = posicao + 1;
        
        for (int i = 0; i < posicao; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
        }
        
        int digito = 11 - (soma % 11);
        if (digito > 9) {
            digito = 0;
        }
        
        return digito == Character.getNumericValue(cpf.charAt(posicao));
    }
}