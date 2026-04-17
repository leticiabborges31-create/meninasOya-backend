package com.projeto.meninas.Service;

import com.projeto.meninas.Entity.Foto;
import com.projeto.meninas.Repository.FotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FotoService {

    private final FotoRepository repository;

    public Foto salvarFoto(Foto foto) {

        return repository.save(foto);
    }

    public List<Foto> listarFotos() {

        return repository.findAll();
    }

    public void deletarFoto(Long id) {

        repository.deleteById(id);
    }
}
