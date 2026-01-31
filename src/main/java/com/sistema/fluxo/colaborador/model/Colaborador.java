package com.sistema.fluxo.colaborador.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Entidade que representa os colaboradores/funcionários da retífica.
 * Exemplos: Marcos, Pedro, Antonio
 */
@Entity
@Table(
    name = "colaboradores",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_colaborador_nome", columnNames = "nome"),
        @UniqueConstraint(name = "uk_colaborador_telefone", columnNames = "telefone")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Colaborador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 200)
    private String nome;

    @Column(unique = true,nullable = false, length = 20)
    private String telefone;

    @Column(nullable = false)
    private Boolean ativo = true; // Indica se o colaborador está ativo

    @Column(length = 500)
    private String observacoes;
}
