package com.projeto.meninas.Controller;

import com.projeto.meninas.Controller.dto.LoginRequest;
import com.projeto.meninas.Controller.dto.LoginResponse;
import com.projeto.meninas.Repository.UsuarioRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;

import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public TokenController(JwtEncoder jwtEncoder,
                           UsuarioRepository usuarioRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        var usuario = usuarioRepository.findByUsername(loginRequest.username());

        if (usuario.isEmpty() ||
                !bCryptPasswordEncoder.matches(
                        loginRequest.password(),
                        usuario.get().getPassword()
                )) {

            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        // 🔐 GERAR TOKEN
        Instant now = Instant.now();

        var claims = JwtClaimsSet.builder()
                .issuer("meninas-api")
                .subject(usuario.get().getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .build();

        String token = jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();

        return ResponseEntity.ok(new LoginResponse(token));
    }
}