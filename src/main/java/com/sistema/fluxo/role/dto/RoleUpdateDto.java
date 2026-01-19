package com.sistema.fluxo.role.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO para atualização de uma Role.
 * Todos os campos são opcionais; validações aplicam-se quando não nulos.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateDto {

    @Size(max = 30, message = "O nome da role não pode exceder 30 caracteres.")
    @Pattern(regexp = "^ROLE_[A-Z0-9_]+$", message = "Use o padrão ROLE_NOME (apenas maiúsculas, números e _). Ex.: ROLE_USER")
    private String name;
}
