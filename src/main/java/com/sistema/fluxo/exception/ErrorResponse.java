package com.sistema.fluxo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {

    // 1. Campos para armazenar os dados do erro
    private int status;
    private String message;
    private long timestamp;

}

