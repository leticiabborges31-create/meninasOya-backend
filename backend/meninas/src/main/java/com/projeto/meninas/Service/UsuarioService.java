package com.projeto.meninas.Service;

import com.projeto.meninas.Controller.dto.LoginRequest;
import com.projeto.meninas.Entity.Usuario;
import com.projeto.meninas.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtEncoder jwtEncoder;

    // ==========================
    // CADASTRAR
    // ==========================
    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    // ==========================
    // BUSCAR POR USERNAME
    // ==========================
    public Usuario buscarUsuarioPorUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // ==========================
    // DELETAR
    // ==========================
    public void deletarUsuarioPorUsername(String username) {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        repository.delete(usuario);
    }

    // ==========================
    // ATUALIZAR
    // ==========================
    public Usuario atualizarUsuarioPorId(UUID id, Usuario usuario) {
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getUsername() != null)
            entity.setUsername(usuario.getUsername());

        if (usuario.getPassword() != null)
            entity.setPassword(encoder.encode(usuario.getPassword()));

        return repository.save(entity);
    }

    // ==========================
    // LOGIN
    // ==========================
    public String autenticar(LoginRequest request) {
        Usuario usuario = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!encoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        return gerarToken(usuario);
    }

    // ==========================
    // GERAR JWT
    // ==========================
    private String gerarToken(Usuario usuario) {
        Instant now = Instant.now();
        long expiry = 3600L; // 1 hora

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("Meninas")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(usuario.getUsername())
                .claim("role", usuario.getRoles())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}