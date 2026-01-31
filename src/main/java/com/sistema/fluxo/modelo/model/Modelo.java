package com.sistema.fluxo.modelo.model;

import com.sistema.fluxo.classificacao.model.Classificacao;
import com.sistema.fluxo.fabricante.model.Fabricante;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Entidade que representa os modelos de motores.
 * Exemplos: D229, TD229, FH D13
 * Relacionado com Fabricante e Classificacao
 */
@Entity
@Table(name = "modelos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Modelo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome; // D229, TD229, FH D13

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fabricante_id", nullable = false)
    private Fabricante fabricante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classificacao_id", nullable = false)
    private Classificacao classificacao;

    @Column(length = 500)
    private String observacoes;
}

