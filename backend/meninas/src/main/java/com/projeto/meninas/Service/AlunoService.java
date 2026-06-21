package com.projeto.meninas.Service;


import com.projeto.meninas.DTO.AlunoRequest;
import com.projeto.meninas.Entity.Aluno;
import com.projeto.meninas.Entity.Cidade;
import com.projeto.meninas.Entity.Escola;
import com.projeto.meninas.Repository.AlunoRepository;
import com.projeto.meninas.Repository.CidadeRepository;
import com.projeto.meninas.Repository.EscolaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final EscolaRepository escolaRepository;
    private final CidadeRepository cidadeRepository;

    public AlunoService(AlunoRepository alunoRepository, EscolaRepository escolaRepository,
                        CidadeRepository cidadeRepository) {
        this.alunoRepository = alunoRepository;
        this.escolaRepository = escolaRepository;
        this.cidadeRepository = cidadeRepository;
    }

    public List<Aluno> listarTodos(String q) {
        if (q == null || q.isBlank()) {
            return alunoRepository.findAll();
        }
        return alunoRepository.findByNomeContainingIgnoreCase(q);
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    public Aluno criar(AlunoRequest req) {
        Escola escola = req.escolaId() != null
                ? escolaRepository.findById(req.escolaId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Escola não encontrada"))
                : null;
        Cidade cidade = req.cidadeId() != null
                ? cidadeRepository.findById(req.cidadeId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cidade não encontrada"))
                : null;
        Aluno aluno = Aluno.builder()
                .nome(req.nome())
                .idade(req.idade())
                .cidade(cidade)
                .escola(escola)
                .vinculo(req.vinculo())
                .build();
        return alunoRepository.save(aluno);
    }

    public Aluno atualizar(Long id, AlunoRequest req) {
        Aluno existente = buscarPorId(id);
        if (req.nome() != null) existente.setNome(req.nome());
        if (req.idade() != null) existente.setIdade(req.idade());
        if (req.cidadeId() != null) {
            Cidade cidade = cidadeRepository.findById(req.cidadeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cidade não encontrada"));
            existente.setCidade(cidade);
        }
        if (req.escolaId() != null) {
            Escola escola = escolaRepository.findById(req.escolaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Escola não encontrada"));
            existente.setEscola(escola);
        }
        return alunoRepository.save(existente);
    }

    public void deletar(Long id) {
        Aluno existente = buscarPorId(id);
        alunoRepository.delete(existente);
    }
}
