package com.sistema.fluxo.fabricante.service;

import com.sistema.fluxo.exception.ResourceConflictException;
import com.sistema.fluxo.exception.ResourceNotFoundException;
import com.sistema.fluxo.fabricante.dto.FabricanteRequestDTO;
import com.sistema.fluxo.fabricante.dto.FabricanteResponseDTO;
import com.sistema.fluxo.fabricante.model.Fabricante;
import com.sistema.fluxo.fabricante.repository.FabricanteRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FabricanteService {

    private final FabricanteRepository repository;

    public List<FabricanteResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(FabricanteResponseDTO::new)
            .collect(Collectors.toList());
    }

    public FabricanteResponseDTO findById(@NonNull Long id) {
        Fabricante fabricante = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado com ID: " + id));
        return new FabricanteResponseDTO(fabricante);
    }

    public FabricanteResponseDTO findByNome(@NonNull String nome) {
        Fabricante fabricante = repository.findByNome(nome)
            .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado com nome: " + nome));
        return new FabricanteResponseDTO(fabricante);
    }

    @Transactional
    public FabricanteResponseDTO save(@NonNull FabricanteRequestDTO dto) {
        if (repository.existsByNome(dto.getNome())) {
            throw new ResourceConflictException("Já existe um fabricante com o nome: " + dto.getNome());
        }
        
        Fabricante fabricante = new Fabricante();
        fabricante.setNome(dto.getNome());
        
        Fabricante saved = repository.save(fabricante);
        return new FabricanteResponseDTO(saved);
    }

    @Transactional
    public FabricanteResponseDTO update(@NonNull Long id, @NonNull FabricanteRequestDTO dto) {
        Fabricante existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado com ID: " + id));
        
        // Verifica se o novo nome já existe em outro registro
        if (!existing.getNome().equals(dto.getNome()) && 
            repository.existsByNome(dto.getNome())) {
            throw new ResourceConflictException("Já existe um fabricante com o nome: " + dto.getNome());
        }
        
        existing.setNome(dto.getNome());
        
        Fabricante updated = repository.save(existing);
        return new FabricanteResponseDTO(updated);
    }

    @Transactional
    public void delete(@NonNull Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Fabricante não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
