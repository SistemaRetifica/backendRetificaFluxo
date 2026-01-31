package com.sistema.fluxo.fabricante.controller;

import com.sistema.fluxo.fabricante.dto.FabricanteRequestDTO;
import com.sistema.fluxo.fabricante.dto.FabricanteResponseDTO;
import com.sistema.fluxo.fabricante.service.FabricanteService;
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
@RequestMapping("/api/fabricantes")
@RequiredArgsConstructor
@Tag(name = "Fabricantes", description = "Gerenciamento de fabricantes de motores")
public class FabricanteController {

    private final FabricanteService service;

    @GetMapping
    @Operation(summary = "Listar todos os fabricantes")
    public ResponseEntity<List<FabricanteResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fabricante por ID")
    public ResponseEntity<FabricanteResponseDTO> findById(@NonNull @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar fabricante por nome")
    public ResponseEntity<FabricanteResponseDTO> findByNome(@NonNull @PathVariable String nome) {
        return ResponseEntity.ok(service.findByNome(nome));
    }

    @PostMapping
    @Operation(summary = "Criar novo fabricante")
    public ResponseEntity<FabricanteResponseDTO> create(@Valid @RequestBody FabricanteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar fabricante")
    public ResponseEntity<FabricanteResponseDTO> update(
            @NonNull @PathVariable Long id,
            @Valid @RequestBody FabricanteRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar fabricante")
    public ResponseEntity<Void> delete(@NonNull @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
