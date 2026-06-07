package com.projeto.meninas.Controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProfessorRequestDto(
        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @NotBlank(message = "CPF e obrigatorio")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 digitos numericos (sem pontuacao)")
        String cpf,

        @NotBlank(message = "Senha e obrigatoria")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String senha,

        @NotBlank(message = "Nome e obrigatorio")
        String nome,

        Integer idade,

        @NotNull(message = "Cidade é obrigatória")
        Long cidadeId,

        @NotBlank(message = "Escola e obrigatoria")
        String escola,

        String linkCurriculoLattes,

        LocalDate periodoVigenciaInicio,

        LocalDate periodoVigenciaFim
) {
}

