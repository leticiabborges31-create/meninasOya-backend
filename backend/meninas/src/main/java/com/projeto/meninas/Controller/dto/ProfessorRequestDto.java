package com.projeto.meninas.Controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

        @Min(value = 0, message = "Idade invalida")
        @Max(value = 150, message = "Idade invalida")
        Integer idade,

        @NotBlank(message = "UF e obrigatoria")
        @Size(min = 2, max = 2, message = "UF deve ser a sigla com 2 letras")
        String uf,

        @NotBlank(message = "Escola e obrigatoria")
        String escola,

        String linkCurriculoLattes
) {
}

