package com.sistema.fluxo.role.service;

import com.sistema.fluxo.exception.ResourceConflictException;
import com.sistema.fluxo.exception.ResourceNotFoundException;
import com.sistema.fluxo.role.dto.RoleCreateDto;
import com.sistema.fluxo.role.dto.RoleResponseDto;
import com.sistema.fluxo.role.dto.RoleUpdateDto;
import com.sistema.fluxo.role.model.Role;
import com.sistema.fluxo.role.repository.RoleRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleResponseDto> findAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<RoleResponseDto> findRoleById(@NonNull Long id) {
        return roleRepository.findById(id)
                .map(this::convertToResponseDto);
    }

    public RoleResponseDto saveRole(@NonNull RoleCreateDto roleCreateDto) {
        if (roleRepository.findByName(roleCreateDto.getName()).isPresent()) {
            throw new ResourceConflictException("A role '" + roleCreateDto.getName() + "' já existe.");
        }

        Role role = new Role();
        role.setName(roleCreateDto.getName());
        
        Role savedRole = Objects.requireNonNull(
            roleRepository.save(role),
            "Erro interno: falha ao salvar a role no banco de dados"
        );
        return convertToResponseDto(savedRole);
    }


    public @NonNull RoleResponseDto updateRole(@NonNull Long id, @NonNull RoleUpdateDto roleUpdateDto) {
        // 1. Orquestração e Infraestrutura
        Role existingRole = Objects.requireNonNull(
            roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role com ID " + id +
                " não encontrada.")));

        // 2. Chamada da Lógica de Negócio
        applyRoleUpdates(existingRole, roleUpdateDto);

        // 3. Persistência e Retorno
        Role savedRole = roleRepository.save(existingRole);
        
        return Objects.requireNonNull(
            convertToResponseDto(savedRole),
            "Erro interno: falha ao atualizar a role no banco de dados"
        );
    }

    private void applyRoleUpdates(Role role, RoleUpdateDto dto) {
        // Validação do nome
        if (dto.getName() != null && !dto.getName().isBlank()) {
            if (!role.getName().equals(dto.getName())) {
                validateNameUniqueness(dto.getName());
                role.setName(dto.getName());
            }
        }
    }

    private void validateNameUniqueness(String nome) {
        if (roleRepository.findByName(nome).isPresent()) {
            throw new ResourceConflictException("A role '" + nome + "' já existe.");
        }
    }
    
    public void deleteRole(@NonNull Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role com ID " + id + " não encontrada.");
        }
        roleRepository.deleteById(id);
    }

    private RoleResponseDto convertToResponseDto(@NonNull Role role) {
        return new RoleResponseDto(role.getId(), role.getName());
    }
}