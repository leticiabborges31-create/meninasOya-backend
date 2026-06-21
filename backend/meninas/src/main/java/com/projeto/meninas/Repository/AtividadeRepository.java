package com.projeto.meninas.Repository;

import com.projeto.meninas.Entity.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeRepository
        extends JpaRepository<Atividade, Long> {

    List<Atividade> findByProfessorUsuarioUsernameIgnoreCase(String username);

    @Query("SELECT a FROM Atividade a WHERE " +
           "(:uf IS NULL OR UPPER(a.professor.cidade.uf) = UPPER(:uf)) AND " +
           "(:cidadeId IS NULL OR a.professor.cidade.id = :cidadeId)")
    List<Atividade> findComFiltros(@Param("uf") String uf, @Param("cidadeId") Long cidadeId);
}