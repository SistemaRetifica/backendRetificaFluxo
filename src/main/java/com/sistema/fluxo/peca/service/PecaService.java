package com.sistema.fluxo.peca.service;

import com.sistema.fluxo.peca.dto.PecaRequestDTO;
import com.sistema.fluxo.peca.dto.PecaResponseDTO;
import com.sistema.fluxo.peca.model.Peca;
import com.sistema.fluxo.peca.repository.PecaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PecaService {

    private final PecaRepository repository;

    public List<PecaResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(PecaResponseDTO::new)
            .collect(Collectors.toList());
    }

    public PecaResponseDTO findById(@NonNull Long id) {
        Peca peca = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Peça não encontrada com ID: " + id));
        return new PecaResponseDTO(peca);
    }

    public PecaResponseDTO findByNome(String nome) {
        Peca peca = repository.findByNome(nome)
            .orElseThrow(() -> new RuntimeException("Peça não encontrada com nome: " + nome));
        return new PecaResponseDTO(peca);
    }

    public List<PecaResponseDTO> findByUsinagem(Boolean usinagem) {
        return repository.findByUsinagem(usinagem).stream()
            .map(PecaResponseDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public PecaResponseDTO save(PecaRequestDTO dto) {
        if (repository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Já existe uma peça com o nome: " + dto.getNome());
        }
        
        Peca peca = new Peca();
        peca.setNome(dto.getNome());
        peca.setUsinagem(dto.getUsinagem());
        
        Peca savedPeca = repository.save(peca);
        return new PecaResponseDTO(savedPeca);
    }

    @Transactional
    public PecaResponseDTO update(@NonNull Long id, PecaRequestDTO dto) {
        Peca existing = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Peça não encontrada com ID: " + id));
        
        // Verifica se o novo nome já existe em outro registro
        if (!existing.getNome().equals(dto.getNome()) && 
            repository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Já existe uma peça com o nome: " + dto.getNome());
        }
        
        existing.setNome(dto.getNome());
        existing.setUsinagem(dto.getUsinagem());
        
        Peca updatedPeca = repository.save(existing);
        return new PecaResponseDTO(updatedPeca);
    }

    @Transactional
    public void delete(@NonNull Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Peça não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
