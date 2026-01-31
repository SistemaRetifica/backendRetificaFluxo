package com.sistema.fluxo.colaborador.dto;

import com.sistema.fluxo.colaborador.model.Colaborador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorResponseDTO {

    private Long id;
    private String nome;
    private String telefone;
    private Boolean ativo;
    private String observacoes;

    // Construtor de conversão
    public ColaboradorResponseDTO(Colaborador colaborador) {
        this.id = colaborador.getId();
        this.nome = colaborador.getNome();
        this.telefone = colaborador.getTelefone();
        this.ativo = colaborador.getAtivo();
        this.observacoes = colaborador.getObservacoes();
    }
}

