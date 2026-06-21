package com.projeto.meninas.Controller;

import com.projeto.meninas.Controller.dto.CreateUserDto;
import com.projeto.meninas.Entity.Role;
import com.projeto.meninas.Entity.Usuario;
import com.projeto.meninas.Repository.RoleRepository;
import com.projeto.meninas.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Transactional
    @PostMapping("/usuario")
    public ResponseEntity<Void> newUser(@Valid @RequestBody CreateUserDto dto) {
        var roleFromDb = roleRepository.findByName(dto.role())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role nao encontrada"));

        if (usuarioRepository.findByUsernameIgnoreCase(dto.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Usuario ja cadastrado");
        }

        usuarioRepository.save(buildUser(dto.username(), dto.password(), roleFromDb));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<Usuario>> listUsers() {
        var usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    private Usuario buildUser(String username, String rawPassword, Role role) {
        var usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        usuario.setRoles(new HashSet<>(Set.of(role)));
        return usuario;
    }
}
