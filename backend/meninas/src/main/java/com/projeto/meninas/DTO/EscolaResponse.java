package com.projeto.meninas.DTO;

import com.projeto.meninas.Entity.Cidade;
import com.projeto.meninas.Entity.EsferaAdministrativa;
import com.projeto.meninas.Entity.NaturezaJuridica;
import com.projeto.meninas.Entity.NivelEducacional;
import com.projeto.meninas.Entity.TipoEscola;

public record EscolaResponse(
        Long id,
        String nome,
        TipoEscola tipo,
        Cidade cidade,
        String emailContato,
        long totalAlunos,
        NivelEducacional nivelEducacional,
        NaturezaJuridica natureza,
        EsferaAdministrativa esferaAdministrativa
) {}
