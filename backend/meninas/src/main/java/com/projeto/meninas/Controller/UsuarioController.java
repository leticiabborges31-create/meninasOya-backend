package com.projeto.meninas.Controller;

import com.projeto.meninas.Entity.Usuario;
import com.projeto.meninas.Service.UsuarioService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // ✅ CADASTRO
    @PostMapping
    public ResponseEntity<Void> salvarUsuario(@RequestBody Usuario usuario) {
        usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok().build();
    }

    // ✅ BUSCAR
    @GetMapping
    public ResponseEntity<Usuario> buscarPorUsername(@RequestParam String username) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorUsername(username));
    }

    // ✅ DELETAR
    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestParam String username) {
        usuarioService.deletarUsuarioPorUsername(username);
        return ResponseEntity.ok().build();
    }

    // ✅ ATUALIZAR
    @PutMapping
    public ResponseEntity<Usuario> atualizar(@RequestParam UUID id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.atualizarUsuarioPorId(id, usuario));
    }
}