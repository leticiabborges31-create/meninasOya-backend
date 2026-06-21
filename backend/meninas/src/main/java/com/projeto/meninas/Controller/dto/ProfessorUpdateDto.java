package com.projeto.meninas.Controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProfessorUpdateDto(
        @Email(message = "Email invalido")
        String email,

        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String senha,

        String nome,

        Integer idade,

        Long cidadeId,

        String escola,

        String linkCurriculoLattes,

        LocalDate periodoVigenciaInicio,

        LocalDate periodoVigenciaFim
) {
}
