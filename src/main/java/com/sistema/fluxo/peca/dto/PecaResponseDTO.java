package com.sistema.fluxo.peca.dto;

import com.sistema.fluxo.peca.model.Peca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PecaResponseDTO {

    private Long id;
    private String nome;
    private Boolean usinagem;

    // Construtor de conversão
    public PecaResponseDTO(Peca peca) {
        this.id = peca.getId();
        this.nome = peca.getNome();
        this.usinagem = peca.getUsinagem();
    }
}
