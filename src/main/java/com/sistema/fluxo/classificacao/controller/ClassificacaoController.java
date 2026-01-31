package com.sistema.fluxo.classificacao.controller;

import com.sistema.fluxo.classificacao.dto.ClassificacaoRequestDTO;
import com.sistema.fluxo.classificacao.dto.ClassificacaoResponseDTO;
import com.sistema.fluxo.classificacao.service.ClassificacaoService;
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
@RequestMapping("/api/classificacoes")
@RequiredArgsConstructor
@Tag(name = "Classificações", description = "Gerenciamento de classificações de serviços")
public class ClassificacaoController {

    private final ClassificacaoService service;

    @GetMapping
    @Operation(summary = "Listar todas as classificações")
    public ResponseEntity<List<ClassificacaoResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar classificação por ID")
    public ResponseEntity<ClassificacaoResponseDTO> findById(@NonNull @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar classificação por nome")
    public ResponseEntity<ClassificacaoResponseDTO> findByNome(@NonNull @PathVariable String nome) {
        return ResponseEntity.ok(service.findByNome(nome));
    }

    @PostMapping
    @Operation(summary = "Criar nova classificação")
    public ResponseEntity<ClassificacaoResponseDTO> create(@Valid @RequestBody ClassificacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar classificação")
    public ResponseEntity<ClassificacaoResponseDTO> update(
            @NonNull @PathVariable Long id,
            @Valid @RequestBody ClassificacaoRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar classificação")
    public ResponseEntity<Void> delete(@NonNull @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
