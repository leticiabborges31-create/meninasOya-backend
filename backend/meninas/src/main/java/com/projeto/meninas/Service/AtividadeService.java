package com.projeto.meninas.Service;

import com.projeto.meninas.Entity.Atividade;
import com.projeto.meninas.Entity.Professor;
import com.projeto.meninas.Repository.AtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository repository;
    private final ProfessorService professorService;

    public Atividade salvar(Atividade atividade, String username) {
        Professor professor = professorService.buscarPorUsername(username);
        atividade.setProfessor(professor);
        return repository.save(atividade);
    }

    public Atividade atualizar(Long id, Atividade payload, String username, boolean isAdmin) {
        Atividade existente = buscarPorId(id);

        if (!isAdmin && !existente.getProfessor().getUsuario().getUsername().equalsIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Professor nao pode editar atividade de outro professor");
        }

        existente.setTitulo(payload.getTitulo());
        existente.setDescricao(payload.getDescricao());
        existente.setData(payload.getData());

        if (payload.getFotoDados() != null) {
            existente.setFotoNomeArquivo(payload.getFotoNomeArquivo());
            existente.setFotoContentType(payload.getFotoContentType());
            existente.setFotoDados(payload.getFotoDados());
        }

        return repository.save(existente);
    }

    public List<Atividade> listarAtividades() {
        return repository.findAll();
    }

    public Atividade buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Atividade nao encontrada"));
    }

    public void deletar(Long id, String username, boolean isAdmin) {
        Atividade atividade = buscarPorId(id);

        if (!isAdmin && !atividade.getProfessor().getUsuario().getUsername().equalsIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Professor nao pode excluir atividade de outro professor");
        }

        repository.delete(atividade);
    }
}
