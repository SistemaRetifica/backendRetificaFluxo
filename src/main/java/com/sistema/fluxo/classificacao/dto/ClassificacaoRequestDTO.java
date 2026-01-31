package com.sistema.fluxo.classificacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificacaoRequestDTO {

    @NotBlank(message = "O nome da classificação é obrigatório")
    @Size(max = 10, message = "O nome deve ter no máximo 10 caracteres")
    private String nome;

    @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres")
    private String descricao;
}
