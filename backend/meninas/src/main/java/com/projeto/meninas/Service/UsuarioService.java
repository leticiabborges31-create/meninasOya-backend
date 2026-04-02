package com.projeto.meninas.Service;

import com.projeto.meninas.Entity.Usuario;
import com.projeto.meninas.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ✅ CADASTRAR
    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    // ✅ BUSCAR POR USERNAME
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

        if (usuario.getUsername() != null)
            entity.setUsername(usuario.getUsername());

        if (usuario.getPassword() != null)
            entity.setPassword(encoder.encode(usuario.getPassword()));

        return repository.save(entity);
    }

    // ✅ LOGIN
    public boolean autenticar(String username, String password) {
        return repository.findByUsername(username)
                .map(u -> encoder.matches(password, u.getPassword()))
                .orElse(false);
    }
}