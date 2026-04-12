package com.projeto.meninas.Service;

import com.projeto.meninas.Entity.Atividade;
import com.projeto.meninas.Repository.AtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository repository;

    public Atividade salvar(Atividade atividade) {
        return repository.save(atividade);
    }

    public List<Atividade> listarAtividades() {
        return repository.findAll();
    }

    public Optional<Atividade> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
