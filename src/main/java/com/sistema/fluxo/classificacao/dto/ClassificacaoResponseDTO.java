package com.sistema.fluxo.classificacao.dto;

import com.sistema.fluxo.classificacao.model.Classificacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificacaoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;

    // Construtor de conversão
    public ClassificacaoResponseDTO(Classificacao classificacao) {
        this.id = classificacao.getId();
        this.nome = classificacao.getNome();
        this.descricao = classificacao.getDescricao();
    }
}