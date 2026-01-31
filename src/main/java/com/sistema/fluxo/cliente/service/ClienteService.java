package com.sistema.fluxo.cliente.service;

import com.sistema.fluxo.cliente.dto.ClienteRequestDTO;
import com.sistema.fluxo.cliente.dto.ClienteResponseDTO;
import com.sistema.fluxo.cliente.model.Cliente;
import com.sistema.fluxo.cliente.repository.ClienteRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository repository;

    public List<ClienteResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(ClienteResponseDTO::new)
            .collect(Collectors.toList());
    }

    public ClienteResponseDTO findById(@NonNull Long id) {
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        return new ClienteResponseDTO(cliente);
    }

    public List<ClienteResponseDTO> findByNome(@NonNull String nome) {
        return repository.findByNomeContainingIgnoreCase(nome).stream()
            .map(ClienteResponseDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public ClienteResponseDTO save(@NonNull ClienteRequestDTO dto) {
        // Valida nome único
        if (repository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Já existe um cliente com o nome: " + dto.getNome());
        }
        
        // Valida telefone único se fornecido
        if (dto.getTelefone() != null && !dto.getTelefone().isBlank() 
            && repository.existsByTelefone(dto.getTelefone())) {
            throw new RuntimeException("Já existe um cliente com o telefone: " + dto.getTelefone());
        }
        
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());
        cliente.setObservacoes(dto.getObservacoes());
        
        Cliente saved = repository.save(cliente);
        return new ClienteResponseDTO(saved);
    }

    @Transactional
    public ClienteResponseDTO update(@NonNull Long id, @NonNull ClienteRequestDTO dto) {
        Cliente existing = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        
        // Valida nome único se alterado
        if (!existing.getNome().equals(dto.getNome()) && repository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Já existe um cliente com o nome: " + dto.getNome());
        }
        
        // Valida telefone único se alterado
        if (dto.getTelefone() != null && !dto.getTelefone().isBlank()) {
            if (!dto.getTelefone().equals(existing.getTelefone()) 
                && repository.existsByTelefone(dto.getTelefone())) {
                throw new RuntimeException("Já existe um cliente com o telefone: " + dto.getTelefone());
            }
        }
        
        existing.setNome(dto.getNome());
        existing.setTelefone(dto.getTelefone());
        existing.setEmail(dto.getEmail());
        existing.setObservacoes(dto.getObservacoes());
        
        Cliente updated = repository.save(existing);
        return new ClienteResponseDTO(updated);
    }

    @Transactional
    public void delete(@NonNull Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
