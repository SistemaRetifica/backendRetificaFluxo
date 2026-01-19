package com.sistema.fluxo.user.controller;

import com.sistema.fluxo.exception.ResourceConflictException;
import com.sistema.fluxo.exception.ResourceNotFoundException;
import com.sistema.fluxo.user.dto.UserCreateDTO;
import com.sistema.fluxo.user.dto.UserResponseDTO;
import com.sistema.fluxo.user.dto.UserUpdateDTO;
import com.sistema.fluxo.user.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Controlador REST responsável por gerenciar operações relacionadas a usuários.
 * Expõe endpoints para criação, listagem, busca, atualização e exclusão de usuários.
 */
@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;


    /**
     * Construtor para injeção de dependência do UserService.
     * 
     * @param userService Serviço responsável pela lógica de negócio dos usuários
     */

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registra um novo usuário no sistema.
     * 
     * Endpoint: POST /api/users/register
     * 
     * Funcionalidade:
     * - Valida os dados de entrada através do @Valid
     * - Cria um novo usuário com as informações fornecidas
     * - Associa roles ao usuário se fornecidas no DTO
     * - Codifica a senha antes de salvar
     * 
     * @param userCreateDTO DTO contendo os dados do novo usuário (username, email, password, roleNames)
     * @return ResponseEntity com o usuário criado (sem senha) e status HTTP 201 CREATED
     * @throws ResourceConflictException se username ou email já estiverem em uso
     * @throws ResourceNotFoundException se alguma role especificada não existir
     */

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @NonNull @RequestBody UserCreateDTO userCreateDTO) {
        UserResponseDTO savedUser = userService.saveUser(userCreateDTO);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Retorna a lista de todos os usuários cadastrados no sistema.
     * 
     * Endpoint: GET /api/users
     * 
     * Funcionalidade:
     * - Busca todos os usuários no banco de dados
     * - Converte as entidades para DTOs (sem expor senhas)
     * - Retorna a lista com informações seguras dos usuários
     * 
     * @return ResponseEntity com lista de UserResponseDTO e status HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Busca um usuário específico por seu ID.
     * 
     * Endpoint: GET /api/users/{id}
     * 
     * Funcionalidade:
     * - Busca o usuário pelo ID fornecido
     * - Retorna os dados do usuário sem expor informações sensíveis
     * - Lança exceção se o usuário não for encontrado
     * 
     * @param id ID do usuário a ser buscado
     * @return ResponseEntity com UserResponseDTO e status HTTP 200 OK
     * @throws ResourceNotFoundException se o usuário com o ID especificado não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable @NonNull Long id) {
        UserResponseDTO user = userService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não encontrado."));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Atualiza os dados de um usuário existente.
     * 
     * Endpoint: PUT /api/users/{id}
     * 
     * Funcionalidade:
     * - Valida os dados de entrada através do @Valid
     * - Atualiza apenas os campos fornecidos no DTO (atualização parcial)
     * - Verifica unicidade de username e email se forem alterados
     * - Codifica a nova senha se fornecida
     * - Preserva os dados não informados
     * 
     * @param id ID do usuário a ser atualizado
     * @param userUpdateDTO DTO contendo os campos a serem atualizados (username, email, password)
     * @return ResponseEntity com o usuário atualizado e status HTTP 200 OK
     * @throws ResourceNotFoundException se o usuário com o ID especificado não existir
     * @throws ResourceConflictException se o novo username ou email já estiverem em uso
     */    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable @NonNull Long id, 
                                                      @Valid @RequestBody @NonNull UserUpdateDTO userUpdateDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userUpdateDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    
    /**
     * Remove um usuário do sistema.
     * 
     * Endpoint: DELETE /api/users/{id}
     * 
     * Funcionalidade:
     * - Verifica se o usuário existe antes de deletar
     * - Remove o usuário permanentemente do banco de dados
     * - Retorna resposta sem conteúdo em caso de sucesso
     * 
     * @param id ID do usuário a ser removido
     * @return ResponseEntity vazio com status HTTP 204 NO CONTENT
     * @throws ResourceNotFoundException se o usuário com o ID especificado não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NonNull Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
