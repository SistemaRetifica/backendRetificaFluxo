package com.sistema.fluxo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// import java.util.Set; // Não necessário se não for usar roles aqui

/**
 * DTO para receber dados de atualização de um usuário.
 * Contém apenas os campos que podem ser modificados via API.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os argumentos
public class UserUpdateDTO {

    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.")
    private String username;

    @Email(message = "Por favor, forneça um endereço de e-mail válido.")
    @Size(max = 100, message = "O e-mail não pode exceder 100 caracteres.")
    private String email;

    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    private String password; // A senha pode ser opcionalmente atualizada

    // Se você tiver roles e quiser permitir a atualização de roles via este DTO:
    // private Set<String> roles; // Exemplo: um set de nomes de roles
}
