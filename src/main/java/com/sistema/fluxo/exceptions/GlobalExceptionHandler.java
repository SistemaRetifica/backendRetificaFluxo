package com.sistema.fluxo.exceptions;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

 // UM ÚNICO HANDLER PARA GOVERNAR TODOS!
    @ExceptionHandler(ApiBaseException.class)
    public ResponseEntity<ErrorResponse> handleApiBaseException(ApiBaseException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getStatus().value(),
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}
