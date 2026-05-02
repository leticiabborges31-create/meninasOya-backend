package com.projeto.meninas.Service;


import com.projeto.meninas.Entity.Aluno;
import com.projeto.meninas.Repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
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

    public Aluno criar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public Aluno atualizar(Long id, Aluno aluno) {
        Aluno existente = buscarPorId(id);

        if (aluno.getNome() != null) {
            existente.setNome(aluno.getNome());
        }
        if (aluno.getIdade() != null) {
            existente.setIdade(aluno.getIdade());
        }
        if (aluno.getEstado() != null) {
            existente.setEstado(aluno.getEstado());
        }
        if (aluno.getEscola() != null) {
            existente.setEscola(aluno.getEscola());
        }

        return alunoRepository.save(existente);
    }

    public void deletar(Long id) {
        Aluno existente = buscarPorId(id);
        alunoRepository.delete(existente);
    }
}
