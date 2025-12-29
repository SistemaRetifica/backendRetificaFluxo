package com.sistema.fluxo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sistema.fluxo.dto.UserUpdateDTO;
import com.sistema.fluxo.exceptions.ResourceConflictException;
import com.sistema.fluxo.exceptions.ResourceNotFoundException;
import com.sistema.fluxo.model.User;
import com.sistema.fluxo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(@NonNull Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public @NonNull User saveUser(@NonNull User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResourceConflictException("O nome de usuário '" + user.getUsername() + "' já está em uso.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceConflictException("O e-mail '" + user.getEmail() + "' já está em uso.");
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    public @NonNull User updateUser(@NonNull Long id, @NonNull UserUpdateDTO userUpdateDTO) {

        User existingUser = Objects.requireNonNull(
            userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não encontrado."))
        );
        applyUserUpdates(existingUser, userUpdateDTO);   
        return userRepository.save(existingUser);
    }

    private void applyUserUpdates(@NonNull User existingUser, @NonNull UserUpdateDTO userUpdateDTO) {
        // Atualiza username
        if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().isEmpty()) {
            if (!existingUser.getUsername().equals(userUpdateDTO.getUsername())) {
                if (userRepository.existsByUsername(userUpdateDTO.getUsername())) {
                    throw new ResourceConflictException("O nome de usuário '" + userUpdateDTO.getUsername() + "' já está em uso por outro usuário.");
                }
                existingUser.setUsername(userUpdateDTO.getUsername());
            }
        }

        // Atualiza email
        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().isEmpty()) {
            if (!existingUser.getEmail().equals(userUpdateDTO.getEmail())) {
                if (userRepository.existsByEmail(userUpdateDTO.getEmail())) {
                    throw new ResourceConflictException("O e-mail '" + userUpdateDTO.getEmail() + "' já está em uso por outro usuário.");
                }
                existingUser.setEmail(userUpdateDTO.getEmail());
            }
        }

        // Atualiza password (se fornecida)
        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

    }


    public void deleteUser(@NonNull Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceConflictException("Usuário com ID " + id + " não encontrado para deleção.");
        }
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
