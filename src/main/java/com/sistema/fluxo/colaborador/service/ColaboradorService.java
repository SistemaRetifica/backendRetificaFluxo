package com.sistema.fluxo.colaborador.service;

import com.sistema.fluxo.colaborador.dto.ColaboradorRequestDTO;
import com.sistema.fluxo.colaborador.dto.ColaboradorResponseDTO;
import com.sistema.fluxo.colaborador.model.Colaborador;
import com.sistema.fluxo.colaborador.repository.ColaboradorRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ColaboradorService {

    private final ColaboradorRepository repository;

    public List<ColaboradorResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(ColaboradorResponseDTO::new)
            .collect(Collectors.toList());
    }

    public ColaboradorResponseDTO findById(@NonNull Long id) {
        Colaborador colaborador = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + id));
        return new ColaboradorResponseDTO(colaborador);
    }

    public List<ColaboradorResponseDTO> findByNome(@NonNull String nome) {
        return repository.findByNomeContainingIgnoreCase(nome).stream()
            .map(ColaboradorResponseDTO::new)
            .collect(Collectors.toList());
    }

    public List<ColaboradorResponseDTO> findByAtivo(@NonNull Boolean ativo) {
        return repository.findByAtivo(ativo).stream()
            .map(ColaboradorResponseDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public ColaboradorResponseDTO save(@NonNull ColaboradorRequestDTO dto) {
        // Valida nome único
        if (repository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Já existe um colaborador com o nome: " + dto.getNome());
        }
        
        // Valida telefone único se fornecido
        if (dto.getTelefone() != null && !dto.getTelefone().isBlank() 
            && repository.existsByTelefone(dto.getTelefone())) {
            throw new RuntimeException("Já existe um colaborador com o telefone: " + dto.getTelefone());
        }
        
        Colaborador colaborador = new Colaborador();
        colaborador.setNome(dto.getNome());
        colaborador.setTelefone(dto.getTelefone());
        colaborador.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        colaborador.setObservacoes(dto.getObservacoes());
        
        Colaborador saved = repository.save(colaborador);
        return new ColaboradorResponseDTO(saved);
    }

    @Transactional
    public ColaboradorResponseDTO update(@NonNull Long id, @NonNull ColaboradorRequestDTO dto) {
        Colaborador existing = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + id));
        
        // Valida nome único se alterado
        if (!existing.getNome().equals(dto.getNome()) && repository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Já existe um colaborador com o nome: " + dto.getNome());
        }
        
        // Valida telefone único se alterado
        if (dto.getTelefone() != null && !dto.getTelefone().isBlank()) {
            if (!dto.getTelefone().equals(existing.getTelefone()) 
                && repository.existsByTelefone(dto.getTelefone())) {
                throw new RuntimeException("Já existe um colaborador com o telefone: " + dto.getTelefone());
            }
        }
        
        existing.setNome(dto.getNome());
        existing.setTelefone(dto.getTelefone());
        existing.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : existing.getAtivo());
        existing.setObservacoes(dto.getObservacoes());
        
        Colaborador updated = repository.save(existing);
        return new ColaboradorResponseDTO(updated);
    }

    @Transactional
    public void delete(@NonNull Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Colaborador não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public ColaboradorResponseDTO inativar(@NonNull Long id) {
        Colaborador colaborador = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + id));
        
        colaborador.setAtivo(false);
        Colaborador updated = repository.save(colaborador);
        return new ColaboradorResponseDTO(updated);
    }

    @Transactional
    public ColaboradorResponseDTO ativar(@NonNull Long id) {
        Colaborador colaborador = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + id));
        
        colaborador.setAtivo(true);
        Colaborador updated = repository.save(colaborador);
        return new ColaboradorResponseDTO(updated);
    }
}







// package com.sistema.fluxo.colaborador.service;

// import com.sistema.fluxo.colaborador.dto.ColaboradorRequestDTO;
// import com.sistema.fluxo.colaborador.dto.ColaboradorResponseDTO;
// import com.sistema.fluxo.colaborador.model.Colaborador;
// import com.sistema.fluxo.colaborador.repository.ColaboradorRepository;
// import lombok.NonNull;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// @Transactional(readOnly = true)
// public class ColaboradorService {

//     private final ColaboradorRepository repository;

//     public List<ColaboradorResponseDTO> findAll() {
//         return repository.findAll().stream()
//             .map(ColaboradorResponseDTO::new)
//             .collect(Collectors.toList());
//     }

//     public ColaboradorResponseDTO findById(@NonNull Long id) {
//         Colaborador colaborador = repository.findById(id)
//             .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + id));
//         return new ColaboradorResponseDTO(colaborador);
//     }

//     public List<ColaboradorResponseDTO> findByNome(@NonNull String nome) {
//         return repository.findByNomeContainingIgnoreCase(nome).stream()
//             .map(ColaboradorResponseDTO::new)
//             .collect(Collectors.toList());
//     }



//     public List<ColaboradorResponseDTO> findByAtivo(@NonNull Boolean ativo) {
//         return repository.findByAtivo(ativo).stream()
//             .map(ColaboradorResponseDTO::new)
//             .collect(Collectors.toList());
//     }

//     @Transactional
//     public ColaboradorResponseDTO save(@NonNull ColaboradorRequestDTO dto) {
//         Colaborador colaborador = new Colaborador();
//         colaborador.setNome(dto.getNome());
//         colaborador.setTelefone(dto.getTelefone());
//         colaborador.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
//         colaborador.setObservacoes(dto.getObservacoes());
        
//         Colaborador saved = repository.save(colaborador);
//         return new ColaboradorResponseDTO(saved);
//     }

//     @Transactional
//     public ColaboradorResponseDTO update(@NonNull Long id, @NonNull ColaboradorRequestDTO dto) {
//         Colaborador existing = repository.findById(id)
//             .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + id));
        
//         existing.setNome(dto.getNome());
//         existing.setTelefone(dto.getTelefone());
//         existing.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : existing.getAtivo());
//         existing.setObservacoes(dto.getObservacoes());
        
//         Colaborador updated = repository.save(existing);
//         return new ColaboradorResponseDTO(updated);
//     }

//     @Transactional
//     public void delete(@NonNull Long id) {
//         if (!repository.existsById(id)) {
//             throw new RuntimeException("Colaborador não encontrado com ID: " + id);
//         }
//         repository.deleteById(id);
//     }

//     @Transactional
//     public ColaboradorResponseDTO inativar(@NonNull Long id) {
//         Colaborador colaborador = repository.findById(id)
//             .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + id));
        
//         colaborador.setAtivo(false);
//         Colaborador updated = repository.save(colaborador);
//         return new ColaboradorResponseDTO(updated);
//     }

//     @Transactional
//     public ColaboradorResponseDTO ativar(@NonNull Long id) {
//         Colaborador colaborador = repository.findById(id)
//             .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + id));
        
//         colaborador.setAtivo(true);
//         Colaborador updated = repository.save(colaborador);
//         return new ColaboradorResponseDTO(updated);
//     }
// }
