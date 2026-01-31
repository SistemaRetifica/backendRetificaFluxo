package com.sistema.fluxo.classificacao.service;

import com.sistema.fluxo.classificacao.dto.ClassificacaoRequestDTO;
import com.sistema.fluxo.classificacao.dto.ClassificacaoResponseDTO;
import com.sistema.fluxo.classificacao.model.Classificacao;
import com.sistema.fluxo.classificacao.repository.ClassificacaoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassificacaoService {

    private final ClassificacaoRepository repository;

    public List<ClassificacaoResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(ClassificacaoResponseDTO::new)
            .collect(Collectors.toList());
    }

    public ClassificacaoResponseDTO findById(@NonNull Long id) {
        Classificacao classificacao = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Classificação não encontrada com ID: " + id));
        return new ClassificacaoResponseDTO(classificacao);
    }

    public ClassificacaoResponseDTO findByNome(@NonNull String nome) {
        Classificacao classificacao = repository.findByNome(nome)
            .orElseThrow(() -> new RuntimeException("Classificação não encontrada com nome: " + nome));
        return new ClassificacaoResponseDTO(classificacao);
    }

    @Transactional
    public ClassificacaoResponseDTO save(@NonNull ClassificacaoRequestDTO dto) {
        if (repository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Já existe uma classificação com o nome: " + dto.getNome());
        }
        
        Classificacao classificacao = new Classificacao();
        classificacao.setNome(dto.getNome());
        classificacao.setDescricao(dto.getDescricao());
        
        Classificacao saved = repository.save(classificacao);
        return new ClassificacaoResponseDTO(saved);
    }

    @Transactional
    public ClassificacaoResponseDTO update(@NonNull Long id, @NonNull ClassificacaoRequestDTO dto) {
        Classificacao existing = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Classificação não encontrada com ID: " + id));
        
        // Verifica se o novo nome já existe em outro registro
        if (!existing.getNome().equals(dto.getNome()) && 
            repository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Já existe uma classificação com o nome: " + dto.getNome());
        }
        
        existing.setNome(dto.getNome());
        existing.setDescricao(dto.getDescricao());
        
        Classificacao updated = repository.save(existing);
        return new ClassificacaoResponseDTO(updated);
    }

    @Transactional
    public void delete(@NonNull Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Classificação não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
