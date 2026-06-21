package com.projeto.meninas.Service;

import com.projeto.meninas.Controller.dto.LoginRequest;
import com.projeto.meninas.Controller.dto.LoginResponse;
import com.projeto.meninas.Entity.Professor;
import com.projeto.meninas.Entity.ProfessorStatus;
import com.projeto.meninas.Entity.Usuario;
import com.projeto.meninas.Repository.ProfessorRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;





@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ProfessorRepository professorRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public LoginResponse authenticate(LoginRequest request) {

        // 🔍 Buscar usuário por username (case-insensitive)
        Usuario usuario = usuarioRepository
                .findByUsernameIgnoreCase(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuário ou senha incorretos"
                ));

        // 🔐 Validar senha
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuário ou senha incorretos"
            );
        }

        Optional<Professor> professorOpt = professorRepository.findByUsuarioUsernameIgnoreCase(usuario.getUsername());
        if (professorOpt.isPresent()) {
            ProfessorStatus status = professorOpt.get().getStatus();
            if (status == ProfessorStatus.PENDENTE) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Cadastro pendente de aprovacao pelo administrador.");
            }
            if (status == ProfessorStatus.REJEITADO) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Cadastro rejeitado. Entre em contato com a coordenacao.");
            }
        }

        // ⏱️ Expiração (1 hora em segundos)
        int expiresIn = 3600;

        // 🎟️ Gerar token JWT
        String token = gerarToken(usuario, expiresIn);

        return new LoginResponse(token, expiresIn);
    }

    private String gerarToken(Usuario usuario, int expiresIn) {
        Instant now = Instant.now();

        String authorities = usuario.getRoles()
                .stream()
                .map(role -> role.getName().replace("ROLE_", "SCOPE_"))
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("Meninas")
                .subject(usuario.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("authorities", authorities)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
