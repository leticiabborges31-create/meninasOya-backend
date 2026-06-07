package com.projeto.meninas.Repository;

import com.projeto.meninas.Entity.Cidade;
import com.projeto.meninas.Entity.Regiao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    List<Cidade> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    List<Cidade> findByRegiaoOrderByNomeAsc(Regiao regiao);

    List<Cidade> findByUfIgnoreCaseOrderByNomeAsc(String uf);

    List<Cidade> findByUfIgnoreCaseAndNomeContainingIgnoreCaseOrderByNomeAsc(String uf, String nome);
}
