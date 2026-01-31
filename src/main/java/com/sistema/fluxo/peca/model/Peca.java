package com.sistema.fluxo.peca.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Entidade que representa as peças de motores.
 * Exemplos: Bloco, Cabeçote, Virabrequim, Biela, Comando
 */
@Entity
@Table(name = "pecas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Peca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome; // Bloco, Cabeçote, Virabrequim, etc.

    @Column(nullable = false)
    private Boolean usinagem = false; // Indica se a peça aceita usinagem
}
