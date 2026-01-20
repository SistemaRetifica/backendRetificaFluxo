package com.sistema.fluxo.role.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO para criação de uma Role.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateDto {

    @NotBlank(message = "O nome da role é obrigatório.")
    @Size(max = 30, message = "O nome da role não pode exceder 30 caracteres.")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "O nome da role deve conter apenas letras maiúsculas, números e underscore, sem espaços")
    private String name;
}