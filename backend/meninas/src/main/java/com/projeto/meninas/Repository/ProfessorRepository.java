package com.projeto.meninas.Repository;


import com.projeto.meninas.Entity.Professor;
import com.projeto.meninas.Entity.ProfessorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    List<Professor> findByNomeContainingIgnoreCase(String nome);
    Optional<Professor> findByEmailIgnoreCase(String email);
    Optional<Professor> findByCpf(String cpf);
    Optional<Professor> findByUsuarioUsernameIgnoreCase(String username);
    List<Professor> findByStatus(ProfessorStatus status);
}


