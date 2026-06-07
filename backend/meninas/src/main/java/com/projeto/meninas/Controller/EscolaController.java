package com.projeto.meninas.Controller;

import com.projeto.meninas.DTO.EscolaRequest;
import com.projeto.meninas.DTO.EscolaResponse;
import com.projeto.meninas.Entity.Escola;
import com.projeto.meninas.Service.EscolaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/escolas")
public class EscolaController {

    private final EscolaService escolaService;

    public EscolaController(EscolaService escolaService) {
        this.escolaService = escolaService;
    }

    /** Lista todas as escolas com contagem de alunas (para o painel admin). */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_PROFESSOR')")
    public ResponseEntity<List<EscolaResponse>> listarTodas() {
        return ResponseEntity.ok(escolaService.listarTodas());
    }

    /** Lista simplificada para uso em selects (ID + nome + tipo). Público. */
    @GetMapping("/simples")
    public ResponseEntity<List<Escola>> listarSimples() {
        return ResponseEntity.ok(escolaService.listarTodasSimples());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Escola> criar(@Valid @RequestBody EscolaRequest req) {
        Escola salva = escolaService.criar(req);
        return ResponseEntity.created(URI.create("/api/escolas/" + salva.getId())).body(salva);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Escola> atualizar(@PathVariable Long id, @Valid @RequestBody EscolaRequest req) {
        return ResponseEntity.ok(escolaService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        escolaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
