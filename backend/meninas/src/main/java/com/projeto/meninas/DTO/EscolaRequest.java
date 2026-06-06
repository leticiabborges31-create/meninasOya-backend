package com.projeto.meninas.DTO;

import com.projeto.meninas.Entity.EsferaAdministrativa;
import com.projeto.meninas.Entity.NaturezaJuridica;
import com.projeto.meninas.Entity.NivelEducacional;
import com.projeto.meninas.Entity.TipoEscola;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EscolaRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotNull(message = "Tipo é obrigatório") TipoEscola tipo,
        @NotNull(message = "Cidade é obrigatória") Long cidadeId,
        String emailContato,
        NivelEducacional nivelEducacional,
        NaturezaJuridica natureza,
        EsferaAdministrativa esferaAdministrativa
) {}
