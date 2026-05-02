package com.projeto.meninas.Controller;

import com.projeto.meninas.Entity.Foto;
import com.projeto.meninas.Service.FotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foto")
@RequiredArgsConstructor
public class FotoController {

    private final FotoService service;

    @PostMapping
    public ResponseEntity<Foto> salvarFoto(@RequestBody Foto foto) {
        return ResponseEntity.ok(service.salvarFoto(foto));
    }

    @GetMapping
    public ResponseEntity<List<Foto>> listarFotos() {
        return ResponseEntity.ok(service.listarFotos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFoto(@PathVariable Long id) {
        service.deletarFoto(id);
        return ResponseEntity.ok().build();
    }
}
