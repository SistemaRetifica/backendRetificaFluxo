package com.sistema.fluxo.cliente.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Entidade que representa os clientes da retífica.
 * Podem ser pessoas físicas ou outras mecânicas.
 */
@Entity
@Table(
    name = "clientes",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_cliente_nome", columnNames = "nome"),
        @UniqueConstraint(name = "uk_cliente_telefone", columnNames = "telefone")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 200)
    private String nome;

    @Column(unique = true, length = 20)
    private String telefone;

    @Column(length = 100)
    private String email;

    @Column(length = 500)
    private String observacoes;
}
