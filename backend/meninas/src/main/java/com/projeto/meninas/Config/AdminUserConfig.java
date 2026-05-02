package com.projeto.meninas.Config;

import com.projeto.meninas.Entity.Role;
import com.projeto.meninas.Entity.Usuario;
import com.projeto.meninas.Repository.RoleRepository;
import com.projeto.meninas.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    @Value("${admin.username:ADMIN}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
                           UsuarioRepository usuarioRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        var roleAdmin = roleRepository.findByName(Role.Values.ROLE_ADMIN.name())
                .orElseGet(() -> roleRepository.save(new Role(Role.Values.ROLE_ADMIN.name())));

        var usuarioAdmin = usuarioRepository.findByUsername(adminUsername);

        usuarioAdmin.ifPresentOrElse(
                usuario -> {
                    usuario.setUsername(adminUsername);
                    usuario.setPassword(bCryptPasswordEncoder.encode(adminPassword));
                    usuario.setRoles(new HashSet<>(Set.of(roleAdmin)));
                    usuarioRepository.save(usuario);
                    System.out.println("Admin atualizado");
                },
                () -> {
                    var usuario = new Usuario();
                    usuario.setUsername(adminUsername);
                    usuario.setPassword(bCryptPasswordEncoder.encode(adminPassword));
                    usuario.setRoles(new HashSet<>(Set.of(roleAdmin)));
                    usuarioRepository.save(usuario);
                }
        );
    }
}
