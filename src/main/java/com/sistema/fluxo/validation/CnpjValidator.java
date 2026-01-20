package com.sistema.fluxo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CnpjValidator implements ConstraintValidator<ValidCnpj, String> {

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        if (cnpj == null || cnpj.isBlank()) {
            return true;
        }
        
        cnpj = cnpj.replaceAll("[^0-9]", "");
        
        if (cnpj.length() != 14) {
            return false;
        }
        
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }
        
        return validarDigitoVerificador(cnpj, 12) && validarDigitoVerificador(cnpj, 13);
    }
    
    private boolean validarDigitoVerificador(String cnpj, int posicao) {
        int[] pesos = posicao == 12 ? 
            new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2} : 
            new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos[i];
        }
        
        int digito = 11 - (soma % 11);
        if (digito > 9) {
            digito = 0;
        }
        
        return digito == Character.getNumericValue(cnpj.charAt(posicao));
    }
}
