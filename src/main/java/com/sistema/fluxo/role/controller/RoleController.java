package com.sistema.fluxo.role.controller;

import com.sistema.fluxo.exception.ResourceNotFoundException;
import com.sistema.fluxo.role.dto.RoleCreateDto;
import com.sistema.fluxo.role.dto.RoleResponseDto;
import com.sistema.fluxo.role.dto.RoleUpdateDto;
import com.sistema.fluxo.role.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleResponseDto> createRole(@Valid @RequestBody 
                                                      @NonNull RoleCreateDto roleCreateDto) {
        RoleResponseDto savedRole = roleService.saveRole(roleCreateDto);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        List<RoleResponseDto> roles = roleService.findAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable @NonNull Long id) {
        RoleResponseDto role = roleService.findRoleById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role com ID " + id + 
                " não encontrada."));
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDto> updateRole(@PathVariable @NonNull Long id, 
                                                      @Valid @RequestBody 
                                                      @NonNull RoleUpdateDto roleUpdateDto) {
        RoleResponseDto updatedRole = roleService.updateRole(id, roleUpdateDto);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable @NonNull Long id) {
        roleService.deleteRole(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

