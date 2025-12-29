package com.sistema.fluxo.repository; // Ajuste o pacote se necessário

import com.sistema.fluxo.model.User; // Importa a entidade User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface de repositório para a entidade User.
 * Estende JpaRepository para fornecer métodos CRUD básicos e funcionalidades de paginação.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Encontra um usuário pelo seu nome de usuário.
     * @param username O nome de usuário a ser pesquisado.
     * @return Um Optional contendo o usuário, se encontrado.
     */
    Optional<User> findByUsername(String username);

    /**
     * Verifica se um usuário com o nome de usuário especificado existe.
     * @param username O nome de usuário a ser verificado.
     * @return true se um usuário com esse nome de usuário existir, false caso contrário.
     */
    Boolean existsByUsername(String username);

    /**
     * Verifica se um usuário com o e-mail especificado existe.
     * @param email O e-mail a ser verificado.
     * @return true se um usuário com esse e-mail existir, false caso contrário.
     */
    Boolean existsByEmail(String email);
}
