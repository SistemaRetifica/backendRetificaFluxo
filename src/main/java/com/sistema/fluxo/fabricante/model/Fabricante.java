package com.sistema.fluxo.fabricante.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Entidade que representa os fabricantes de motores.
 * Exemplos: MWM, Cummins, Volvo
 */
@Entity
@Table(name = "fabricantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Fabricante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome; // MWM, Cummins, Volvo
}
