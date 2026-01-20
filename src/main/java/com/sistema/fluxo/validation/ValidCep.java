package com.sistema.fluxo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CepValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCep {
    String message() default "CEP inválido. Use o formato: XXXXX-XXX";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
