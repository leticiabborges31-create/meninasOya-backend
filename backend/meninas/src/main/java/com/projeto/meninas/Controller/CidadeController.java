package com.projeto.meninas.Controller;

import com.projeto.meninas.Config.CidadeDataInitializer;
import com.projeto.meninas.Entity.Cidade;
import com.projeto.meninas.Entity.Regiao;
import com.projeto.meninas.Repository.CidadeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cidades")
public class CidadeController {

    private final CidadeRepository cidadeRepository;
    private final CidadeDataInitializer cidadeDataInitializer;

    public CidadeController(CidadeRepository cidadeRepository,
                            CidadeDataInitializer cidadeDataInitializer) {
        this.cidadeRepository = cidadeRepository;
        this.cidadeDataInitializer = cidadeDataInitializer;
    }

    /**
     * Busca cidades com filtros opcionais.
     * Exemplos:
     *   GET /api/cidades?q=São Paulo
     *   GET /api/cidades?uf=SP
     *   GET /api/cidades?regiao=SE
     *   GET /api/cidades?uf=SP&q=camp
     */
    @GetMapping
    public ResponseEntity<List<Cidade>> buscar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) Regiao regiao
    ) {
        List<Cidade> resultado;
        if (uf != null && !uf.isBlank() && q != null && !q.isBlank()) {
            resultado = cidadeRepository.findByUfIgnoreCaseAndNomeContainingIgnoreCaseOrderByNomeAsc(uf, q);
        } else if (uf != null && !uf.isBlank()) {
            resultado = cidadeRepository.findByUfIgnoreCaseOrderByNomeAsc(uf);
        } else if (regiao != null) {
            resultado = cidadeRepository.findByRegiaoOrderByNomeAsc(regiao);
        } else if (q != null && !q.isBlank()) {
            resultado = cidadeRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(q);
        } else {
            resultado = cidadeRepository.findAll();
        }
        return ResponseEntity.ok(resultado);
    }

    /**
     * Aciona o seed manual via API IBGE.
     * Útil quando a aplicação subiu sem internet na primeira vez.
     * Só pode ser chamado por ADMIN.
     */
    @PostMapping("/seed")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Map<String, Object>> seed() {
        long antes = cidadeRepository.count();
        long carregadas = cidadeDataInitializer.carregarCidades();
        return ResponseEntity.ok(Map.of(
                "cidadesAntes", antes,
                "cidadesCarregadas", carregadas,
                "totalAtual", cidadeRepository.count()
        ));
    }
}
