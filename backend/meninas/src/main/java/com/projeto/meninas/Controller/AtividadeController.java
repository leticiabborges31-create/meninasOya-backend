package com.projeto.meninas.Controller;

import com.projeto.meninas.Entity.Atividade;
import com.projeto.meninas.Service.AtividadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
            @RequestPart(required = false) MultipartFile foto,
            Authentication authentication
    ) {
        Atividade atividade = montarAtividade(titulo, descricao, data, foto);
        Atividade salva = atividadeService.salvar(atividade, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Atividade> atualizar(
            @PathVariable Long id,
            @RequestPart String titulo,
            @RequestPart String descricao,
            @RequestPart String data,
            @RequestPart(required = false) MultipartFile foto,
            Authentication authentication
    ) {
        Atividade atividade = montarAtividade(titulo, descricao, data, foto);
        Atividade atualizada = atividadeService.atualizar(id, atividade, authentication.getName(), isAdmin(authentication));
        return ResponseEntity.ok(atualizada);
    }

    @GetMapping
    public ResponseEntity<List<Atividade>> listar() {
        return ResponseEntity.ok(atividadeService.listarAtividades());
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, Authentication authentication) {
        atividadeService.deletar(id, authentication.getName(), isAdmin(authentication));
        return ResponseEntity.ok().build();
    }

    private Atividade montarAtividade(String titulo, String descricao, String data, MultipartFile foto) {
        Atividade atividade = new Atividade();
        atividade.setTitulo(titulo);
        atividade.setDescricao(descricao);
        atividade.setData(data);

        if (foto != null && !foto.isEmpty()) {
            try {
                atividade.setFotoNomeArquivo(foto.getOriginalFilename());
                atividade.setFotoContentType(foto.getContentType());
                atividade.setFotoDados(foto.getBytes());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nao foi possivel ler a foto", e);
            }
        }

        return atividade;
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("SCOPE_ADMIN"));
    }
}