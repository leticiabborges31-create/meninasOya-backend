package com.projeto.meninas.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlunoRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @Min(0) @Max(150) Integer idade,
        @NotBlank @Size(min = 2, max = 2, message = "UF deve ser a sigla com 2 letras") String uf,
        Long escolaId
) {}
