package com.projeto.meninas.Service;

import com.projeto.meninas.Controller.dto.ProfessorRequestDto;
import com.projeto.meninas.Controller.dto.ProfessorUpdateDto;
import com.projeto.meninas.Entity.Cidade;
import com.projeto.meninas.Entity.Professor;
import com.projeto.meninas.Entity.ProfessorStatus;
import com.projeto.meninas.Entity.Role;
import com.projeto.meninas.Entity.Usuario;
import com.projeto.meninas.Repository.CidadeRepository;
import com.projeto.meninas.Repository.ProfessorRepository;
import com.projeto.meninas.Repository.RoleRepository;
import com.projeto.meninas.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final CidadeRepository cidadeRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Professor> listarTodos(String q) {
        if (q == null || q.isBlank()) {
            return professorRepository.findAll();
        }
        return professorRepository.findByNomeContainingIgnoreCase(q);
    }

    public List<Professor> listarPorStatus(ProfessorStatus status) {
        return professorRepository.findByStatus(status);
    }

    public Professor buscarPorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor nao encontrado"));
    }

    public Professor buscarPorUsername(String username) {
        return professorRepository.findByUsuarioUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor nao encontrado"));
    }

    /** Criado por um ADMIN — ja sai aprovado. */
    public Professor criar(ProfessorRequestDto dto) {
        return persistirNovo(dto, ProfessorStatus.APROVADO);
    }

    public Professor cadastrarPendente(ProfessorRequestDto dto) {
        return persistirNovo(dto, ProfessorStatus.PENDENTE);
    }

    public Professor aprovar(Long id) {
        Professor professor = buscarPorId(id);
        professor.setStatus(ProfessorStatus.APROVADO);
        return professorRepository.save(professor);
    }

    public Professor rejeitar(Long id) {
        Professor professor = buscarPorId(id);
        professor.setStatus(ProfessorStatus.REJEITADO);
        return professorRepository.save(professor);
    }

    private Professor persistirNovo(ProfessorRequestDto dto, ProfessorStatus status) {
        validarEmailDisponivel(dto.email(), null);
        validarCpfDisponivel(dto.cpf(), null);

        Role roleProfessor = roleRepository.findByName(Role.Values.ROLE_PROFESSOR.name())
                .orElseGet(() -> roleRepository.save(new Role(Role.Values.ROLE_PROFESSOR.name())));

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.email().trim().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(dto.senha()));
        usuario.setRoles(new HashSet<>(Set.of(roleProfessor)));
        usuario = usuarioRepository.save(usuario);

        Cidade cidade = cidadeRepository.findById(dto.cidadeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cidade não encontrada"));

        Professor professor = Professor.builder()
                .email(dto.email().trim().toLowerCase())
                .cpf(dto.cpf().replaceAll("\\D", ""))
                .nome(dto.nome())
                .idade(dto.idade())
                .cidade(cidade)
                .escola(dto.escola())
                .linkCurriculoLattes(dto.linkCurriculoLattes())
                .periodoVigenciaInicio(dto.periodoVigenciaInicio())
                .periodoVigenciaFim(dto.periodoVigenciaFim())
                .status(status)
                .usuario(usuario)
                .build();

        return professorRepository.save(professor);
    }

    public Professor atualizar(Long id, ProfessorUpdateDto dto) {
        Professor existente = buscarPorId(id);
        Usuario usuario = existente.getUsuario();

        if (dto.email() != null && !dto.email().isBlank()) {
            String emailNormalizado = dto.email().trim().toLowerCase();
            validarEmailDisponivel(emailNormalizado, existente.getId());
            existente.setEmail(emailNormalizado);
            usuario.setUsername(emailNormalizado);
        }
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.senha()));
        }
        if (dto.nome() != null && !dto.nome().isBlank()) {
            existente.setNome(dto.nome());
        }
        if (dto.idade() != null) {
            existente.setIdade(dto.idade());
        }
        if (dto.cidadeId() != null) {
            Cidade cidade = cidadeRepository.findById(dto.cidadeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cidade não encontrada"));
            existente.setCidade(cidade);
        }
        if (dto.escola() != null && !dto.escola().isBlank()) {
            existente.setEscola(dto.escola());
        }
        if (dto.linkCurriculoLattes() != null && !dto.linkCurriculoLattes().isBlank()) {
            existente.setLinkCurriculoLattes(dto.linkCurriculoLattes());
        }
        if (dto.periodoVigenciaInicio() != null) {
            existente.setPeriodoVigenciaInicio(dto.periodoVigenciaInicio());
        }
        if (dto.periodoVigenciaFim() != null) {
            existente.setPeriodoVigenciaFim(dto.periodoVigenciaFim());
        }
        usuarioRepository.save(usuario);
        return professorRepository.save(existente);
    }

    public void resetarSenha(Long id, String novaSenha) {
        Professor professor = buscarPorId(id);
        Usuario usuario = professor.getUsuario();
        usuario.setPassword(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        Professor existente = buscarPorId(id);
        Usuario usuario = existente.getUsuario();
        professorRepository.delete(existente);
        usuarioRepository.delete(usuario);
    }

    public boolean professorPodeGerenciar(Long professorId, String username) {
        Professor professor = buscarPorUsername(username);
        return professor.getId().equals(professorId);
    }

    private void validarCpfDisponivel(String cpfRaw, Long professorIdIgnorado) {
        if (cpfRaw == null) return;
        String cpf = cpfRaw.replaceAll("\\D", "");
        professorRepository.findByCpf(cpf)
                .filter(professor -> professorIdIgnorado == null || !professor.getId().equals(professorIdIgnorado))
                .ifPresent(professor -> {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CPF ja cadastrado");
                });
    }

    private void validarEmailDisponivel(String email, Long professorIdIgnorado) {
        professorRepository.findByEmailIgnoreCase(email)
                .filter(professor -> professorIdIgnorado == null || !professor.getId().equals(professorIdIgnorado))
                .ifPresent(professor -> {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email ja cadastrado para professor");
                });

        usuarioRepository.findByUsernameIgnoreCase(email)
                .filter(usuario -> professorIdIgnorado == null || professorRepository.findByUsuarioUsernameIgnoreCase(usuario.getUsername())
                        .map(Professor::getId)
                        .filter(professorIdIgnorado::equals)
                        .isEmpty())
                .ifPresent(usuario -> {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email ja cadastrado para usuario");
                });
    }
}

