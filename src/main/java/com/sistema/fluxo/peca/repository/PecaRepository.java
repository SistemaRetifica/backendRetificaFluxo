package com.sistema.fluxo.peca.repository;

import com.sistema.fluxo.peca.model.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PecaRepository extends JpaRepository<Peca, Long> {
    
    Optional<Peca> findByNome(String nome);
    
    boolean existsByNome(String nome);
    
    List<Peca> findByUsinagem(Boolean usinagem); // Buscar peças que aceitam/não aceitam usinagem
}