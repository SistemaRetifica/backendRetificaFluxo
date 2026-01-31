package com.sistema.fluxo.modelo.dto;

import com.sistema.fluxo.classificacao.dto.ClassificacaoResponseDTO;
import com.sistema.fluxo.fabricante.dto.FabricanteResponseDTO;
import com.sistema.fluxo.modelo.model.Modelo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloResponseDTO {

    private Long id;
    private String nome;
    private FabricanteResponseDTO fabricante;
    private ClassificacaoResponseDTO classificacao;
    private String observacoes;

    // Construtor de conversão
    public ModeloResponseDTO(Modelo modelo) {
        this.id = modelo.getId();
        this.nome = modelo.getNome();
        this.fabricante = new FabricanteResponseDTO(modelo.getFabricante());
        this.classificacao = new ClassificacaoResponseDTO(modelo.getClassificacao());
        this.observacoes = modelo.getObservacoes();
    }
}

