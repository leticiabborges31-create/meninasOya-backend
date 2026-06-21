package com.projeto.meninas.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import com.projeto.meninas.Entity.Vinculo;

public record AlunoRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @Min(0) @Max(150) Integer idade,
        Long cidadeId,
        Long escolaId,
        Vinculo vinculo
) {}
