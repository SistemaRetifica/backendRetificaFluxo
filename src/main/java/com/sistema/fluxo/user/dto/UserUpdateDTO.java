package com.sistema.fluxo.user.dto;

import com.sistema.fluxo.validation.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.")
    private String username;

    @Email(message = "O e-mail deve ser válido.")
    @Size(max = 100, message = "O e-mail não pode exceder 100 caracteres.")
    private String email;

    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    @ValidPassword
    private String password; // A senha pode ser opcionalmente atualizada

}
