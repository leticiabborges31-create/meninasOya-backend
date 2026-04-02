package com.projeto.meninas.Controller;

import com.projeto.meninas.Entity.Usuario;
import com.projeto.meninas.Service.UsuarioService;
import com.projeto.meninas.Controller.dto.LoginRequest;
import com.projeto.meninas.Controller.dto.LoginResponse;

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

    // =========================
    // CADASTRO
    // =========================
    @PostMapping
    public ResponseEntity<Void> salvarUsuario(@RequestBody Usuario usuario) {
        usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok().build();
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = usuarioService.login(request);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    // =========================
    // BUSCAR POR USERNAME
    // =========================
    @GetMapping
    public ResponseEntity<Usuario> buscarPorUsername(@RequestParam String username) {
        Usuario usuario = usuarioService.buscarUsuarioPorUsername(username);
        return ResponseEntity.ok(usuario);
    }

    // =========================
    // DELETAR
    // =========================
    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestParam String username) {
        usuarioService.deletarUsuarioPorUsername(username);
        return ResponseEntity.ok().build();
    }

    // =========================
    // ATUALIZAR
    // =========================
    @PutMapping
    public ResponseEntity<Usuario> atualizar(@RequestParam UUID id, @RequestBody Usuario usuario) {
        Usuario atualizado = usuarioService.atualizarUsuarioPorId(id, usuario);
        return ResponseEntity.ok(atualizado);
    }
}