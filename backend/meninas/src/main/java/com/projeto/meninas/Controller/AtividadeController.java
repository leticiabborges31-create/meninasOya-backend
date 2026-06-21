package com.projeto.meninas.Controller;

import com.projeto.meninas.Entity.Atividade;
import com.projeto.meninas.Service.AtividadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class AtividadeController {

    private final AtividadeService atividadeService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Atividade> salvar(
            @RequestPart String titulo,
            @RequestPart String descricao,
            @RequestPart String data,
            @RequestPart(required = false) String localizacao,
            @RequestPart(required = false) MultipartFile foto,
            @RequestPart(required = false) MultipartFile foto2,
            @RequestPart(required = false) String professorId,
            Authentication authentication
    ) {
        Atividade atividade = montarAtividade(titulo, descricao, data, localizacao, foto, foto2);
        Atividade salva = atividadeService.salvar(atividade, authentication.getName(), isAdmin(authentication), professorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Atividade> atualizar(
            @PathVariable Long id,
            @RequestPart String titulo,
            @RequestPart String descricao,
            @RequestPart String data,
            @RequestPart(required = false) String localizacao,
            @RequestPart(required = false) MultipartFile foto,
            @RequestPart(required = false) MultipartFile foto2,
            Authentication authentication
    ) {
        Atividade atividade = montarAtividade(titulo, descricao, data, localizacao, foto, foto2);
        Atividade atualizada = atividadeService.atualizar(id, atividade, authentication.getName(), isAdmin(authentication));
        return ResponseEntity.ok(atualizada);
    }

    @GetMapping
    public ResponseEntity<List<Atividade>> listar(
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) Long cidadeId) {
        return ResponseEntity.ok(atividadeService.listarAtividades(uf, cidadeId));
    }

    @GetMapping("/minhas")
    @PreAuthorize("hasAuthority('SCOPE_PROFESSOR')")
    public ResponseEntity<List<Atividade>> listarMinhas(Authentication authentication) {
        return ResponseEntity.ok(atividadeService.listarPorProfessor(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atividade> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(atividadeService.buscarPorId(id));
    }

    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> buscarFoto(@PathVariable Long id) {
        var atividade = atividadeService.buscarPorId(id);

        if (!atividade.isTemFoto()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atividade sem foto");
        }

        var contentType = atividade.getFotoContentType() != null
                ? MediaType.parseMediaType(atividade.getFotoContentType())
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + atividade.getFotoNomeArquivo() + "\"")
                .body(atividade.getFotoDados());
    }

    @GetMapping("/{id}/foto2")
    public ResponseEntity<byte[]> buscarFoto2(@PathVariable Long id) {
        var atividade = atividadeService.buscarPorId(id);

        if (!atividade.isTemFoto2()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atividade sem segunda foto");
        }

        var contentType = atividade.getFoto2ContentType() != null
                ? MediaType.parseMediaType(atividade.getFoto2ContentType())
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + atividade.getFoto2NomeArquivo() + "\"")
                .body(atividade.getFoto2Dados());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, Authentication authentication) {
        atividadeService.deletar(id, authentication.getName(), isAdmin(authentication));
        return ResponseEntity.ok().build();
    }

    private Atividade montarAtividade(String titulo, String descricao, String data, String localizacao, MultipartFile foto, MultipartFile foto2) {
        Atividade atividade = new Atividade();
        atividade.setTitulo(titulo);
        atividade.setDescricao(descricao);
        atividade.setData(data);
        atividade.setLocalizacao(localizacao);

        if (foto != null && !foto.isEmpty()) {
            try {
                atividade.setFotoNomeArquivo(foto.getOriginalFilename());
                atividade.setFotoContentType(foto.getContentType());
                atividade.setFotoDados(foto.getBytes());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nao foi possivel ler a foto", e);
            }
        }

        if (foto2 != null && !foto2.isEmpty()) {
            try {
                atividade.setFoto2NomeArquivo(foto2.getOriginalFilename());
                atividade.setFoto2ContentType(foto2.getContentType());
                atividade.setFoto2Dados(foto2.getBytes());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nao foi possivel ler a segunda foto", e);
            }
        }

        return atividade;
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("SCOPE_ADMIN"));
    }
}