package com.projeto.meninas.Service;

import com.projeto.meninas.DTO.EscolaRequest;
import com.projeto.meninas.DTO.EscolaResponse;
import com.projeto.meninas.Entity.Escola;
import com.projeto.meninas.Repository.AlunoRepository;
import com.projeto.meninas.Repository.EscolaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EscolaService {

    private final EscolaRepository escolaRepository;
    private final AlunoRepository alunoRepository;

    public EscolaService(EscolaRepository escolaRepository, AlunoRepository alunoRepository) {
        this.escolaRepository = escolaRepository;
        this.alunoRepository = alunoRepository;
    }

    public List<EscolaResponse> listarTodas() {
        return escolaRepository.findAll().stream()
                .map(e -> new EscolaResponse(
                        e.getId(), e.getNome(), e.getTipo(),
                        e.getCidade(), e.getUf(), e.getEmailContato(),
                        alunoRepository.countByEscola_Id(e.getId())))
                .toList();
    }

    public List<Escola> listarTodasSimples() {
        return escolaRepository.findAll();
    }

    public Escola buscarPorId(Long id) {
        return escolaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrada"));
    }

    public Escola criar(EscolaRequest req) {
        Escola escola = Escola.builder()
                .nome(req.nome())
                .tipo(req.tipo())
                .cidade(req.cidade())
                .uf(req.uf())
                .emailContato(req.emailContato())
                .build();
        return escolaRepository.save(escola);
    }

    public Escola atualizar(Long id, EscolaRequest req) {
        Escola existente = buscarPorId(id);
        if (req.nome() != null) existente.setNome(req.nome());
        if (req.tipo() != null) existente.setTipo(req.tipo());
        if (req.cidade() != null) existente.setCidade(req.cidade());
        if (req.uf() != null) existente.setUf(req.uf());
        existente.setEmailContato(req.emailContato());
        return escolaRepository.save(existente);
    }

    public void deletar(Long id) {
        Escola existente = buscarPorId(id);
        long totalAlunos = alunoRepository.countByEscola_Id(id);
        if (totalAlunos > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir: esta escola possui " + totalAlunos + " aluna(s) vinculada(s).");
        }
        escolaRepository.delete(existente);
    }
}
