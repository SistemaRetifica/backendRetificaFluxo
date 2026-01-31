package com.sistema.fluxo.fabricante.dto;

import com.sistema.fluxo.fabricante.model.Fabricante;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FabricanteResponseDTO {

    private Long id;
    private String nome;

    // Construtor de conversão
    public FabricanteResponseDTO(Fabricante fabricante) {
        this.id = fabricante.getId();
        this.nome = fabricante.getNome();
    }
}
