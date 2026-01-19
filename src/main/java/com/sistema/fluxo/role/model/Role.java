package com.sistema.fluxo.role.model; // Ajuste o pacote se necessário

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Entidade que representa um papel (role) ou perfil de acesso no sistema.
 * Define as permissões que um usuário pode ter.
 */
@Entity
@Table(name = "roles") // Tabela para os papéis de usuário
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") // Gerar equals e hashCode baseado no ID
@ToString
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name; // Ex: ROLE_ADMIN, ROLE_USER
}
