package com.sistema.fluxo.colaborador.repository;

import com.sistema.fluxo.colaborador.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    
    List<Colaborador> findByNomeContainingIgnoreCase(String nome);
    
    List<Colaborador> findByAtivo(Boolean ativo);
    
    boolean existsByNome(String nome);
    
    boolean existsByTelefone(String telefone);
    
    Optional<Colaborador> findByNome(String nome);
    
    Optional<Colaborador> findByTelefone(String telefone);
}



// package com.sistema.fluxo.colaborador.repository;

// import com.sistema.fluxo.colaborador.model.Colaborador;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.util.List;

// @Repository
// public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    
//     List<Colaborador> findByNomeContainingIgnoreCase(String nome);
    
//     List<Colaborador> findByAtivo(Boolean ativo);
// }

