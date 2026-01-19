package com.sistema.fluxo.exception;

import org.springframework.http.HttpStatus;

// Não precisa mais do @ResponseStatus aqui!
public class ResourceConflictException extends ApiBaseException {

    public ResourceConflictException(String message) {
        // Informa para a classe "mãe" qual é o seu status e mensagem
        super(HttpStatus.CONFLICT, message);
    }
}

