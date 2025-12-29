package com.sistema.fluxo.exceptions;

import org.springframework.http.HttpStatus;

// Não precisa mais do @ResponseStatus aqui!
public class ResourceNotFoundException extends ApiBaseException {

    public ResourceNotFoundException(String message) {
        // Informa para a classe "mãe" qual é o seu status e mensagem
        super(HttpStatus.NOT_FOUND, message);
    }
}

