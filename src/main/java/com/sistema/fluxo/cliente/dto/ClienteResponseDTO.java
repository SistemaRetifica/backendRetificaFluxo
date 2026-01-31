package com.sistema.fluxo.cliente.dto;

import com.sistema.fluxo.cliente.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String observacoes;

    // Construtor de conversão
    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.telefone = cliente.getTelefone();
        this.email = cliente.getEmail();
        this.observacoes = cliente.getObservacoes();
    }
}

