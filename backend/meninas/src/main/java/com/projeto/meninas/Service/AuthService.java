package com.projeto.meninas.Service;

import com.projeto.meninas.Config.TokenConfig;
import com.projeto.meninas.Controller.dto.LoginRequest;
import com.projeto.meninas.Controller.dto.LoginResponse;
import com.projeto.meninas.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtEncoder jwtEncoder;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenConfig tokenConfig;

    public LoginResponse authenticate(LoginRequest loginRequest) {
        var usuario = usuarioRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new BadCredentialsException("USER OR PASSWORD IS INVALID"));

        if (!usuario.isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("USER OR PASSWORD IS INVALID");
        }

        var now = Instant.now();
        var expiresIn = tokenConfig.getExpiresIn();
        var scopes = usuario.getRoles()
                .stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer(tokenConfig.getIssuer())
                .subject(usuario.getUsuarioId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginResponse(jwtValue, (int) expiresIn);
    }
}
