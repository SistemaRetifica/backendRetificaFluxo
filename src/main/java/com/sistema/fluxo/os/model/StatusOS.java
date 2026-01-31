package com.sistema.fluxo.os.model;

/**
 * Enum que representa os possíveis status de uma Ordem de Serviço
 */
public enum StatusOS {
    ORCAMENTO("Orçamento"),
    AUTORIZACAO("Autorização"),
    PECAS("Peças"),
    PRODUCAO("Produção"),
    FATURANDO("Faturado"),
    ENTREGUE("Entregue"),
    FINALIZADO("Finalizado"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusOS(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
