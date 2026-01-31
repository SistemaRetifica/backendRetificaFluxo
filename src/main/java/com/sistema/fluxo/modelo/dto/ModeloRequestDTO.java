package com.sistema.fluxo.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloRequestDTO {

    @NotBlank(message = "O nome do modelo é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(message = "O ID do fabricante é obrigatório")
    private Long fabricanteId;

    @NotNull(message = "O ID da classificação é obrigatório")
    private Long classificacaoId;

    @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
    private String observacoes;
}
