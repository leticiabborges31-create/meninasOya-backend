package com.projeto.meninas.DTO;

import com.projeto.meninas.Entity.TipoEscola;

public record EscolaResponse(
        Long id,
        String nome,
        TipoEscola tipo,
        String cidade,
        String uf,
        String emailContato,
        long totalAlunos
) {}
