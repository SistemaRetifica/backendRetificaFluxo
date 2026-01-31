package com.sistema.fluxo.modelo.service;

import com.sistema.fluxo.classificacao.model.Classificacao;
import com.sistema.fluxo.classificacao.repository.ClassificacaoRepository;
import com.sistema.fluxo.exception.ResourceConflictException;
import com.sistema.fluxo.exception.ResourceNotFoundException;
import com.sistema.fluxo.fabricante.model.Fabricante;
import com.sistema.fluxo.fabricante.repository.FabricanteRepository;
import com.sistema.fluxo.modelo.dto.ModeloRequestDTO;
import com.sistema.fluxo.modelo.dto.ModeloResponseDTO;
import com.sistema.fluxo.modelo.model.Modelo;
import com.sistema.fluxo.modelo.repository.ModeloRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
@Transactional(readOnly = true)
public class ModeloService {

    private final ModeloRepository repository;
    private final FabricanteRepository fabricanteRepository;
    private final ClassificacaoRepository classificacaoRepository;

    public List<ModeloResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(ModeloResponseDTO::new)
            .collect(Collectors.toList());
    }

    public ModeloResponseDTO findById(@NonNull Long id) {
        Modelo modelo = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Modelo não encontrado com ID: " + id));
        return new ModeloResponseDTO(modelo);
    }

    public List<ModeloResponseDTO> findByNome(@NonNull String nome) {
        return repository.findByNomeContainingIgnoreCase(nome).stream()
            .map(ModeloResponseDTO::new)
            .collect(Collectors.toList());
    }

    public List<ModeloResponseDTO> findByFabricante(@NonNull Long fabricanteId) {
        if (!fabricanteRepository.existsById(fabricanteId)) {
            throw new ResourceNotFoundException("Fabricante não encontrado com ID: " + fabricanteId);
        }
        return repository.findByFabricanteId(fabricanteId).stream()
            .map(ModeloResponseDTO::new)
            .collect(Collectors.toList());
    }

    public List<ModeloResponseDTO> findByClassificacao(@NonNull Long classificacaoId) {
        if (!classificacaoRepository.existsById(classificacaoId)) {
            throw new ResourceNotFoundException("Classificação não encontrada com ID: " + classificacaoId);
        }
        return repository.findByClassificacaoId(classificacaoId).stream()
            .map(ModeloResponseDTO::new)
            .collect(Collectors.toList());
    }

    public List<ModeloResponseDTO> findByFabricanteAndClassificacao(
            @NonNull Long fabricanteId, 
            @NonNull Long classificacaoId) {
        if (!fabricanteRepository.existsById(fabricanteId)) {
            throw new ResourceNotFoundException("Fabricante não encontrado com ID: " + fabricanteId);
        }
        if (!classificacaoRepository.existsById(classificacaoId)) {
            throw new ResourceNotFoundException("Classificação não encontrada com ID: " + classificacaoId);
        }
        return repository.findByFabricanteAndClassificacao(fabricanteId, classificacaoId).stream()
            .map(ModeloResponseDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public ModeloResponseDTO save(@NonNull ModeloRequestDTO dto) {
        Fabricante fabricante = fabricanteRepository.findById(dto.getFabricanteId())
            .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado com ID: " + dto.getFabricanteId()));
        
        Classificacao classificacao = classificacaoRepository.findById(dto.getClassificacaoId())
            .orElseThrow(() -> new ResourceNotFoundException("Classificação não encontrada com ID: " + dto.getClassificacaoId()));
        
        if (repository.existsByNomeAndFabricanteId(dto.getNome(), dto.getFabricanteId())) {
            throw new ResourceConflictException("Já existe um modelo com o nome '" + dto.getNome() 
                + "' para o fabricante " + fabricante.getNome());
        }
        
        Modelo modelo = new Modelo();
        modelo.setNome(dto.getNome());
        modelo.setFabricante(fabricante);
        modelo.setClassificacao(classificacao);
        modelo.setObservacoes(dto.getObservacoes());
        
        Modelo saved = repository.save(modelo);
        return new ModeloResponseDTO(saved);
    }

    @Transactional
    public ModeloResponseDTO update(@NonNull Long id, @NonNull ModeloRequestDTO dto) {
        Modelo existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Modelo não encontrado com ID: " + id));
        
        Fabricante fabricante = fabricanteRepository.findById(dto.getFabricanteId())
            .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado com ID: " + dto.getFabricanteId()));
        
        Classificacao classificacao = classificacaoRepository.findById(dto.getClassificacaoId())
            .orElseThrow(() -> new ResourceNotFoundException("Classificação não encontrada com ID: " + dto.getClassificacaoId()));
        
        // Valida duplicidade apenas se mudou nome ou fabricante
        boolean mudouNome = !existing.getNome().equals(dto.getNome());
        boolean mudouFabricante = !existing.getFabricante().getId().equals(dto.getFabricanteId());
        
        if (mudouNome || mudouFabricante) {
            if (repository.existsByNomeAndFabricanteId(dto.getNome(), dto.getFabricanteId())) {
                throw new ResourceConflictException("Já existe um modelo com o nome '" + dto.getNome() 
                    + "' para o fabricante " + fabricante.getNome());
            }
        }
        
        existing.setNome(dto.getNome());
        existing.setFabricante(fabricante);
        existing.setClassificacao(classificacao);
        existing.setObservacoes(dto.getObservacoes());
        
        Modelo updated = repository.save(existing);
        return new ModeloResponseDTO(updated);
    }

    @Transactional
    public void delete(@NonNull Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Modelo não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}

