package com.sistema.fluxo.cliente.repository;

import com.sistema.fluxo.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    
    boolean existsByNome(String nome);
    
    boolean existsByTelefone(String telefone);
    
    Optional<Cliente> findByNome(String nome);
    
    Optional<Cliente> findByTelefone(String telefone);
    
    Optional<Cliente> findByEmail(String email);
}

