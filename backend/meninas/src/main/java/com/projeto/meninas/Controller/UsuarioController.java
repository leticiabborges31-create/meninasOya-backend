package com.projeto.meninas.Controller;

import com.projeto.meninas.Controller.dto.CreateUserDto;
import com.projeto.meninas.Entity.Role;
import com.projeto.meninas.Entity.Usuario;
import com.projeto.meninas.Repository.RoleRepository;
import com.projeto.meninas.Repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             RoleRepository roleRepository ) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Transactional
    @PostMapping("/usuario")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto){
        var roleFromDb = roleRepository.findByName(dto.role())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role não encontrada"));
        var usuarioFromDb = usuarioRepository.findByUsername(dto.username());
        if(usuarioFromDb.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        var usuario = new Usuario();
        usuario.setUsername(dto.username());
        usuario.setPassword(bCryptPasswordEncoder.encode(dto.password()));
        usuario.setRoles(new HashSet<>(Set.of(roleFromDb)));

        usuarioRepository.save(usuario);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<Usuario>> listUsers(){
        var usuario = usuarioRepository.findAll();
        return ResponseEntity.ok(usuario);
    }

}
