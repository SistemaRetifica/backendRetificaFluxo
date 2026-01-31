package com.sistema.fluxo.modelo.repository;

import com.sistema.fluxo.modelo.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    
    List<Modelo> findByNomeContainingIgnoreCase(String nome);
    
    List<Modelo> findByFabricanteId(Long fabricanteId);
    
    List<Modelo> findByClassificacaoId(Long classificacaoId);
    
    @Query("SELECT m FROM Modelo m WHERE m.fabricante.id = :fabricanteId AND m.classificacao.id = :classificacaoId")
    List<Modelo> findByFabricanteAndClassificacao(
        @Param("fabricanteId") Long fabricanteId, 
        @Param("classificacaoId") Long classificacaoId
    );
    
    Optional<Modelo> findByNomeAndFabricanteId(String nome, Long fabricanteId);
    
    boolean existsByNomeAndFabricanteId(String nome, Long fabricanteId);
}

