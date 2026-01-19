package com.sistema.fluxo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

    @NotBlank(message = "O nome de usuário é obrigatório.")
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.")
    private String username;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Por favor, forneça um endereço de e-mail válido.")
    @Size(max = 100, message = "O e-mail não pode exceder 100 caracteres.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    private String password;

    private Set<String> roleNames; // Para associar roles durante criação
}