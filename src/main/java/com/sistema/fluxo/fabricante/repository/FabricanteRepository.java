package com.sistema.fluxo.fabricante.repository;

import com.sistema.fluxo.fabricante.model.Fabricante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FabricanteRepository extends JpaRepository<Fabricante, Long> {
    
    Optional<Fabricante> findByNome(String nome);
    
    boolean existsByNome(String nome);
}
