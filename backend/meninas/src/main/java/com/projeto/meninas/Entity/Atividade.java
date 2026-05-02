package com.projeto.meninas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(nullable = false)
    private String data;

    private String fotoNomeArquivo;

    private String fotoContentType;

    @Lob
    @JsonIgnore
    private byte[] fotoDados;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    @JsonIgnore
    private Professor professor;

    public boolean isTemFoto() {
        return fotoDados != null && fotoDados.length > 0;
    }

    @JsonProperty("professorId")
    public Long getProfessorId() {
        return professor != null ? professor.getId() : null;
    }

    @JsonProperty("professorNome")
    public String getProfessorNome() {
        return professor != null ? professor.getNome() : null;
    }
}
