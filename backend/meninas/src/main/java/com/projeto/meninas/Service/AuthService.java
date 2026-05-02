package com.projeto.meninas.Service;

import com.projeto.meninas.Controller.dto.LoginRequest;
import com.projeto.meninas.Controller.dto.LoginResponse;
import com.projeto.meninas.Entity.Usuario;
import com.projeto.meninas.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.stream.Collectors;





@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public LoginResponse authenticate(LoginRequest request) {

        // 🔍 Buscar usuário por username (case-insensitive)
        Usuario usuario = usuarioRepository
                .findByUsernameIgnoreCase(request.getUsername()) // ✅ Corrigido: getUsername() em vez de username()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuário ou senha incorretos"
                ));

        // 🔐 Validar senha
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) { // ✅ getPassword()
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuário ou senha incorretos"
            );
        }

        // ⏱️ Expiração (1 hora em segundos)
        int expiresIn = 3600;

        // 🎟️ Gerar token JWT
        String token = gerarToken(usuario, expiresIn);

        return new LoginResponse(token, expiresIn);
    }

    private String gerarToken(Usuario usuario, int expiresIn) {
        // ✅ Timestamp atual
        Instant now = Instant.now();

        // ✅ Converter roles para authorities (SCOPE_)
        String authorities = usuario.getRoles()
                .stream()
                .map(role -> role.getName().replace("ROLE_", "SCOPE_"))
                .collect(Collectors.joining(" "));

        // ✅ Construir claims do JWT
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("Meninas")
                .subject(usuario.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("authorities", authorities)
                .build();

        // ✅ Codificar e retornar token
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}