package com.sistema.fluxo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public abstract class ApiBaseException extends RuntimeException {

    // 1. ANOTE O CAMPO: "Este campo, uma vez inicializado, nunca será nulo."
    @NonNull
    private final HttpStatus status;

    // 2. ANOTE O PARÂMETRO: "Este construtor exige um status que não seja nulo."
    public ApiBaseException(@NonNull HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    // 3. A ANOTAÇÃO NO MÉTODO AGORA FAZ SENTIDO PARA A IDE:
    // "Este método retorna o campo 'status', que eu sei que não é nulo."
    @NonNull
    public HttpStatus getStatus() {
        return status;
    }
}
