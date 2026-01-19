package com.sistema.fluxo.role.repository; // Ajuste o pacote se necessário

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.fluxo.role.model.Role;

import java.util.Optional;

/**
 * Interface de repositório para a entidade Role.
 * Estende JpaRepository para fornecer métodos CRUD básicos e funcionalidades de paginação.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Encontra um papel (role) pelo seu nome.
     * @param name O nome do papel (ex: ROLE_ADMIN, ROLE_USER) a ser pesquisado.
     * @return Um Optional contendo o papel, se encontrado.
     */
    Optional<Role> findByName(String name);
}
