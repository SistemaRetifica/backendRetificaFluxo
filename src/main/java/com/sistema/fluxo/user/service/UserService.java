package com.sistema.fluxo.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sistema.fluxo.exception.ResourceConflictException;
import com.sistema.fluxo.exception.ResourceNotFoundException;
import com.sistema.fluxo.role.dto.RoleResponseDto;
import com.sistema.fluxo.role.model.Role;
import com.sistema.fluxo.role.repository.RoleRepository;
import com.sistema.fluxo.user.dto.UserCreateDTO;
import com.sistema.fluxo.user.dto.UserResponseDTO;
import com.sistema.fluxo.user.dto.UserUpdateDTO;
import com.sistema.fluxo.user.model.User;
import com.sistema.fluxo.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retorna todos os usuários convertidos para DTO.
     */
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca um usuário por ID e retorna como DTO.
     */
    public Optional<UserResponseDTO> findUserById(@NonNull Long id) {
        return userRepository.findById(id)
                .map(this::convertToResponseDto);
    }

    /**
     * Busca um usuário por username (retorna entidade para uso interno).
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Cria um novo usuário a partir do UserCreateDTO.
     */
    public UserResponseDTO saveUser(@NonNull UserCreateDTO userCreateDTO) {
        // Validações de unicidade
        if (userRepository.existsByUsername(userCreateDTO.getUsername())) {
            throw new ResourceConflictException("O nome de usuário '" + userCreateDTO.getUsername() + "' já está em uso.");
        }
        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            throw new ResourceConflictException("O e-mail '" + userCreateDTO.getEmail() + "' já está em uso.");
        }

        // Cria a entidade User
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        // Associa roles se fornecidas
        if (userCreateDTO.getRoleNames() != null && !userCreateDTO.getRoleNames().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : userCreateDTO.getRoleNames()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Role '" + roleName + "' não encontrada."));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        User savedUser = Objects.requireNonNull(
            userRepository.save(user),
            "Erro interno: falha ao salvar o usuário no banco de dados"
        );

        return convertToResponseDto(savedUser);
    }

    /**
     * Atualiza um usuário existente.
     */
    public UserResponseDTO updateUser(@NonNull Long id, @NonNull UserUpdateDTO userUpdateDTO) {

         User existingUser = Objects.requireNonNull(
             userRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não encontrado."))
         );
        
         // Validações de unicidade se username ou email forem alterados
        applyUserUpdates(existingUser, userUpdateDTO);

        User savedUser = Objects.requireNonNull(
            userRepository.save(existingUser),
            "Erro interno: falha ao atualizar o usuário no banco de dados"
        );

        return convertToResponseDto(savedUser);
    }

    /**
     * Aplica as atualizações do DTO na entidade User.
     */
    private void applyUserUpdates(@NonNull User existingUser, @NonNull UserUpdateDTO userUpdateDTO) {
        // Atualiza username
        if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().isBlank()) {
            if (!existingUser.getUsername().equals(userUpdateDTO.getUsername())) {
                if (userRepository.existsByUsername(userUpdateDTO.getUsername())) {
                    throw new ResourceConflictException("O nome de usuário '" + userUpdateDTO.getUsername() + "' já está em uso por outro usuário.");
                }
                existingUser.setUsername(userUpdateDTO.getUsername());
            }
        }

        // Atualiza email
        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().isBlank()) {
            if (!existingUser.getEmail().equals(userUpdateDTO.getEmail())) {
                if (userRepository.existsByEmail(userUpdateDTO.getEmail())) {
                    throw new ResourceConflictException("O e-mail '" + userUpdateDTO.getEmail() + "' já está em uso por outro usuário.");
                }
                existingUser.setEmail(userUpdateDTO.getEmail());
            }
        }

        // Atualiza password (se fornecida)
        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }
    }

    /**
     * Deleta um usuário por ID.
     */
    public void deleteUser(@NonNull Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário com ID " + id + " não encontrado para deleção.");
        }
        userRepository.deleteById(id);
    }

    /**
     * Verifica se existe um usuário com o username fornecido.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Verifica se existe um usuário com o email fornecido.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Converte a entidade User para UserResponseDTO.
     */
    private UserResponseDTO convertToResponseDto(@NonNull User user) {
        Set<RoleResponseDto> roleDtos = user.getRoles().stream()
                .map(role -> new RoleResponseDto(role.getId(), role.getName()))
                .collect(Collectors.toSet());

        return new UserResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            roleDtos
        );
    }
}