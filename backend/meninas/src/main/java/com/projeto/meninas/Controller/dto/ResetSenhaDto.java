package com.projeto.meninas.Controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetSenhaDto(
        @NotBlank(message = "A senha nao pode ser vazia")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String novaSenha
) {
}
