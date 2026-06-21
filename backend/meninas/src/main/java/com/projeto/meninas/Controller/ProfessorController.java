package com.projeto.meninas.Controller;

import com.projeto.meninas.Controller.dto.ProfessorRequestDto;
import com.projeto.meninas.Controller.dto.ProfessorUpdateDto;
import com.projeto.meninas.Controller.dto.ResetSenhaDto;
import com.projeto.meninas.Entity.Professor;
import com.projeto.meninas.Entity.ProfessorStatus;
import com.projeto.meninas.Service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_PROFESSOR')")
    public ResponseEntity<Professor> meuPerfil(Authentication authentication) {
        return ResponseEntity.ok(professorService.buscarPorUsername(authentication.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<Professor>> listarTodos(@RequestParam(value = "q", required = false) String q) {
        return ResponseEntity.ok(professorService.listarTodos(q));
    }

    @GetMapping("/pendentes")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<Professor>> listarPendentes() {
        return ResponseEntity.ok(professorService.listarPorStatus(ProfessorStatus.PENDENTE));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Professor> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.buscarPorId(id));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Professor> cadastrarPublico(@Valid @RequestBody ProfessorRequestDto professor) {
        Professor salvo = professorService.cadastrarPendente(professor);
        return ResponseEntity.created(URI.create("/api/professores/" + salvo.getId())).body(salvo);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Professor> criar(@Valid @RequestBody ProfessorRequestDto professor) {
        Professor salvo = professorService.criar(professor);
        return ResponseEntity.created(URI.create("/api/professores/" + salvo.getId())).body(salvo);
    }

    @PutMapping("/{id}/aprovar")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Professor> aprovar(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.aprovar(id));
    }

    @PutMapping("/{id}/rejeitar")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Professor> rejeitar(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.rejeitar(id));
    }

    @PatchMapping("/{id}/reset-senha")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> resetarSenha(@PathVariable Long id, @Valid @RequestBody ResetSenhaDto dto) {
        professorService.resetarSenha(id, dto.novaSenha());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfessorUpdateDto professor,
            Authentication authentication
    ) {
        validarPermissaoProfessor(id, authentication);
        return ResponseEntity.ok(professorService.atualizar(id, professor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, Authentication authentication) {
        validarPermissaoProfessor(id, authentication);
        professorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private void validarPermissaoProfessor(Long professorId, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("SCOPE_ADMIN"));

        if (!isAdmin && !professorService.professorPodeGerenciar(professorId, authentication.getName())) {
            throw new ResponseStatusException(FORBIDDEN, "Professor nao pode gerenciar outro professor");
        }
    }
}
