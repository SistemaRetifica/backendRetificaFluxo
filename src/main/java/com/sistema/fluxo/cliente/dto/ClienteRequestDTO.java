package com.sistema.fluxo.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {

    @NotBlank(message = "O nome do cliente é obrigatório")
    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres")
    private String nome;

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    private String telefone;

    @Email(message = "E-mail inválido")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
    private String email;

    @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
    private String observacoes;
}
