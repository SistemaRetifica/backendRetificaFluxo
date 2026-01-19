package com.sistema.fluxo.user.dto;

import com.sistema.fluxo.role.dto.RoleResponseDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    
    private Long id;
    private String username;
    private String email;
    private Set<RoleResponseDto> roles;
}
