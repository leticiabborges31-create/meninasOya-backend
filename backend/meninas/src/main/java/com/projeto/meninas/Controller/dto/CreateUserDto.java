package com.projeto.meninas.Controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @NotBlank String username,
        @NotBlank @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres") String password,
        @NotBlank String role
) {
}
