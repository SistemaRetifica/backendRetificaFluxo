package com.sistema.fluxo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handler para exceções customizadas (404, 409, etc.)
    @ExceptionHandler(ApiBaseException.class)
    public ResponseEntity<ErrorResponse> handleApiBaseException(ApiBaseException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getStatus().value(),
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    // Handler para erros inesperados (500)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro interno do servidor: " + ex.getMessage(),
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
