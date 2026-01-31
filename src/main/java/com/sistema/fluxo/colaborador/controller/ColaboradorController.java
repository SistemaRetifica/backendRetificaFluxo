package com.sistema.fluxo.colaborador.controller;

import com.sistema.fluxo.colaborador.dto.ColaboradorRequestDTO;
import com.sistema.fluxo.colaborador.dto.ColaboradorResponseDTO;
import com.sistema.fluxo.colaborador.service.ColaboradorService;
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
@RequestMapping("/api/colaboradores")
@RequiredArgsConstructor
@Tag(name = "Colaboradores", description = "Gerenciamento de colaboradores da retífica")
public class ColaboradorController {

    private final ColaboradorService service;

    @GetMapping
    @Operation(summary = "Listar todos os colaboradores")
    public ResponseEntity<List<ColaboradorResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar colaborador por ID")
    public ResponseEntity<ColaboradorResponseDTO> findById(@NonNull @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar colaboradores por nome (parcial)")
    public ResponseEntity<List<ColaboradorResponseDTO>> findByNome(@NonNull @PathVariable String nome) {
        return ResponseEntity.ok(service.findByNome(nome));
    }

    @GetMapping("/ativo/{ativo}")
    @Operation(summary = "Buscar colaboradores por status ativo/inativo")
    public ResponseEntity<List<ColaboradorResponseDTO>> findByAtivo(@NonNull @PathVariable Boolean ativo) {
        return ResponseEntity.ok(service.findByAtivo(ativo));
    }

    @PostMapping
    @Operation(summary = "Criar novo colaborador")
    public ResponseEntity<ColaboradorResponseDTO> create(@Valid @RequestBody ColaboradorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar colaborador")
    public ResponseEntity<ColaboradorResponseDTO> update(
            @NonNull @PathVariable Long id,
            @Valid @RequestBody ColaboradorRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar colaborador")
    public ResponseEntity<ColaboradorResponseDTO> inativar(@NonNull @PathVariable Long id) {
        return ResponseEntity.ok(service.inativar(id));
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar colaborador")
    public ResponseEntity<ColaboradorResponseDTO> ativar(@NonNull @PathVariable Long id) {
        return ResponseEntity.ok(service.ativar(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar colaborador")
    public ResponseEntity<Void> delete(@NonNull @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
