package com.sistema.fluxo.peca.controller;

import com.sistema.fluxo.peca.dto.PecaRequestDTO;
import com.sistema.fluxo.peca.dto.PecaResponseDTO;
import com.sistema.fluxo.peca.service.PecaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pecas")
@RequiredArgsConstructor
@Tag(name = "Peças", description = "Gerenciamento de peças de motores")
public class PecaController {

    private final PecaService service;

    @GetMapping
    @Operation(summary = "Listar todas as peças")
    public ResponseEntity<List<PecaResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar peça por ID")
    public ResponseEntity<PecaResponseDTO> findById(@NonNull @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar peça por nome")
    public ResponseEntity<PecaResponseDTO> findByNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.findByNome(nome));
    }

    @GetMapping("/usinagem/{usinagem}")
    @Operation(summary = "Buscar peças por disponibilidade de usinagem")
    public ResponseEntity<List<PecaResponseDTO>> findByUsinagem(@PathVariable Boolean usinagem) {
        return ResponseEntity.ok(service.findByUsinagem(usinagem));
    }

    @PostMapping
    @Operation(summary = "Criar nova peça")
    public ResponseEntity<PecaResponseDTO> create(@Valid @RequestBody PecaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar peça")
    public ResponseEntity<PecaResponseDTO> update(
            @NonNull @PathVariable Long id,
            @Valid @RequestBody PecaRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar peça")
    public ResponseEntity<Void> delete(@NonNull @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
