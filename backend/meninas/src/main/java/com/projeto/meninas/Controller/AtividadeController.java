package com.projeto.meninas.Controller;

import com.projeto.meninas.Entity.Atividade;
import com.projeto.meninas.Service.AtividadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AtividadeController {

    private final AtividadeService atividadeService;

    // ✅ SALVAR ATIVIDADE
    @PostMapping
    public ResponseEntity<Atividade> salvar(
            @RequestParam String titulo,
            @RequestParam String descricao,
            @RequestParam String data,
            @RequestParam(required = false) MultipartFile foto
    ) {

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

        Atividade salva = atividadeService.salvar(atividade);

        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    // ✅ LISTAR
    @GetMapping
    public ResponseEntity<List<Atividade>> listar() {
        return ResponseEntity.ok(atividadeService.listarAtividades());
    }

    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> buscarFoto(@PathVariable Long id) {
        var atividade = atividadeService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Atividade nao encontrada"));

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

    // ✅ DELETAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        atividadeService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
