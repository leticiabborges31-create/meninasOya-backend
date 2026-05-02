package com.projeto.meninas.Controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ProfessorUpdateDto(
        @Email(message = "Email invalido")
        String email,

        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String senha,

        String nome,

        @Min(value = 0, message = "Idade invalida")
        @Max(value = 150, message = "Idade invalida")
        Integer idade,

        @Size(min = 2, max = 2, message = "UF deve ser a sigla com 2 letras")
        String uf,

        String escola,

        String linkCurriculoLattes
) {
}
