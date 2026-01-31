package com.sistema.fluxo.classificacao.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Entidade que representa a classificação de serviços e modelos.
 * Exemplos: A,B,C,D,E... (Modelos de motor)
 */
@Entity
@Table(name = "classificacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Classificacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String nome; // Nome da classificação (ex: A, B, C...)

    @Column(length = 200)
    private String descricao; // Opcional: descrição da classificação
}
