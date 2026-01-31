package com.sistema.fluxo.modelo.controller;

import com.sistema.fluxo.modelo.dto.ModeloRequestDTO;
import com.sistema.fluxo.modelo.dto.ModeloResponseDTO;
import com.sistema.fluxo.modelo.service.ModeloService;
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
@RequestMapping("/api/modelos")
@RequiredArgsConstructor
@Tag(name = "Modelos", description = "Gerenciamento de modelos de motores")
public class ModeloController {

    private final ModeloService service;

    @GetMapping
    @Operation(summary = "Listar todos os modelos")
    public ResponseEntity<List<ModeloResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar modelo por ID")
    public ResponseEntity<ModeloResponseDTO> findById(@NonNull @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar modelos por nome (parcial)")
    public ResponseEntity<List<ModeloResponseDTO>> findByNome(@NonNull @PathVariable String nome) {
        return ResponseEntity.ok(service.findByNome(nome));
    }

    @GetMapping("/fabricante/{fabricanteId}")
    @Operation(summary = "Buscar modelos por fabricante")
    public ResponseEntity<List<ModeloResponseDTO>> findByFabricante(@NonNull @PathVariable Long fabricanteId) {
        return ResponseEntity.ok(service.findByFabricante(fabricanteId));
    }

    @GetMapping("/classificacao/{classificacaoId}")
    @Operation(summary = "Buscar modelos por classificação")
    public ResponseEntity<List<ModeloResponseDTO>> findByClassificacao(@NonNull @PathVariable Long classificacaoId) {
        return ResponseEntity.ok(service.findByClassificacao(classificacaoId));
    }

    @GetMapping("/filtro")
    @Operation(summary = "Buscar modelos por fabricante e classificação")
    public ResponseEntity<List<ModeloResponseDTO>> findByFabricanteAndClassificacao(
            @NonNull @RequestParam Long fabricanteId,
            @NonNull @RequestParam Long classificacaoId) {
        return ResponseEntity.ok(service.findByFabricanteAndClassificacao(fabricanteId, classificacaoId));
    }

    @PostMapping
    @Operation(summary = "Criar novo modelo")
    public ResponseEntity<ModeloResponseDTO> create(@Valid @RequestBody ModeloRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar modelo")
    public ResponseEntity<ModeloResponseDTO> update(
            @NonNull @PathVariable Long id,
            @Valid @RequestBody ModeloRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar modelo")
    public ResponseEntity<Void> delete(@NonNull @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

