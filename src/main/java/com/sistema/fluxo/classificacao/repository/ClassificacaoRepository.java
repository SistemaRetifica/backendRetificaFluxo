package com.sistema.fluxo.classificacao.repository;

import com.sistema.fluxo.classificacao.model.Classificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassificacaoRepository extends JpaRepository<Classificacao, Long> {
    
    Optional<Classificacao> findByNome(String nome);
    
    boolean existsByNome(String nome);
}
