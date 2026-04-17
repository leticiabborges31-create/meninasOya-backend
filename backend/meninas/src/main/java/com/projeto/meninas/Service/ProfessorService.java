package com.projeto.meninas.Service;


import com.projeto.meninas.Entity.Professor;
import com.projeto.meninas.Repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public List<Professor> listarTodos(String q) {
        if (q == null || q.isBlank()) {
            return professorRepository.findAll();
        }
        return professorRepository.findByNomeContainingIgnoreCase(q);
    }

    public Professor buscarPorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
    }

    public Professor criar(Professor professor) {
        return professorRepository.save(professor);
    }

    public Professor atualizar(Long id, Professor professor) {
        Professor existente = buscarPorId(id);

        existente.setNome(professor.getNome());
        existente.setIdade(professor.getIdade());
        existente.setUf(professor.getUf());
        existente.setEscola(professor.getEscola());
        existente.setLinkCurriculoLattes(professor.getLinkCurriculoLattes());

        return professorRepository.save(existente);
    }

    public void deletar(Long id) {
        Professor existente = buscarPorId(id);
        professorRepository.delete(existente);
    }
}

