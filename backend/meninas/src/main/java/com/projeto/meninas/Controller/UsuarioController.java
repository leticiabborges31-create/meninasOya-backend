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
    @Transactional
    @PostMapping("/usuario")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto){
        var basicRole = roleRepository.findByName(Role.Values.ROLE_BASIC.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role ROLE_BASIC nao encontrada"));
        var usuarioFromDb = usuarioRepository.findByUsername(dto.username());
        if(usuarioFromDb.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        var usuario = new Usuario();
        usuario.setUsername(dto.username());
        usuario.setPassword(bCryptPasswordEncoder.encode(dto.password()));
        usuario.setRoles(new HashSet<>(Set.of(basicRole)));



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
