package com.sistema.fluxo.cliente.controller;

import com.sistema.fluxo.cliente.dto.ClienteRequestDTO;
import com.sistema.fluxo.cliente.dto.ClienteResponseDTO;
import com.sistema.fluxo.cliente.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gerenciamento de clientes da retífica")
public class ClienteController {

    private final ClienteService service;

    @GetMapping
    @Operation(summary = "Listar todos os clientes")
    public ResponseEntity<List<ClienteResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<ClienteResponseDTO> findById(@NonNull @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar cliente por e-mail")
    public ResponseEntity<ClienteResponseDTO> findByEmail(@NonNull @PathVariable String email) {
        return service.findAll().stream()
                .filter(c -> c.getEmail() != null && c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar clientes por nome (parcial)")
    public ResponseEntity<List<ClienteResponseDTO>> findByNome(@NonNull @PathVariable String nome) {
        return ResponseEntity.ok(service.findByNome(nome));
    }

    @PostMapping
    @Operation(summary = "Criar novo cliente")
    public ResponseEntity<ClienteResponseDTO> create(@Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente")
    public ResponseEntity<ClienteResponseDTO> update(
            @NonNull @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente")
    public ResponseEntity<Void> delete(@NonNull @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
