package com.projeto.meninas.Service;

import com.projeto.meninas.Controller.dto.LoginRequest;
import com.projeto.meninas.Entity.Usuario;
import com.projeto.meninas.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder encoder;

    // ✅ CADASTRAR
    public Usuario salvarUsuario(Usuario usuario) {
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(encoder.encode(usuario.getPassword()));
        }
        return repository.save(usuario);
    }

    // ✅ BUSCAR
    public Usuario buscarUsuarioPorUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // ✅ DELETAR
    public void deletarUsuarioPorUsername(String username) {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        repository.delete(usuario);
    }

    // ✅ ATUALIZAR
    public Usuario atualizarUsuarioPorId(UUID id, Usuario usuario) {
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getUsername() != null && !usuario.getUsername().isBlank()) {
            entity.setUsername(usuario.getUsername());
        }

        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            entity.setPassword(encoder.encode(usuario.getPassword()));
        }

        return repository.save(entity);
    }
}