package com.projeto.meninas.DTO;

import com.projeto.meninas.Entity.TipoEscola;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EscolaRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotNull(message = "Tipo é obrigatório") TipoEscola tipo,
        @NotBlank(message = "Cidade é obrigatória") String cidade,
        @NotBlank @Size(min = 2, max = 2, message = "UF deve ser sigla com 2 letras") String uf,
        String emailContato
) {}
