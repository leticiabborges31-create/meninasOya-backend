package com.projeto.meninas.Controller;

import com.projeto.meninas.Controller.dto.LoginRequest;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final JwtEncoder jwtEncoder;

    public AuthController(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping
    public String login(@RequestBody LoginRequest login) {

        // 🔥 aqui você validaria usuário/senha (simplificado por enquanto)
        if (!login.username().equals("admin") || !login.password().equals("123")) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        var now = Instant.now();

        var claims = JwtClaimsSet.builder()
                .issuer("meninas-api")
                .subject(login.username())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600)) // 1 hora
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}