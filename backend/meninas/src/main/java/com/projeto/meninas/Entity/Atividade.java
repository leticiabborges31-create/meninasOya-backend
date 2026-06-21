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

    @Column(nullable = false, columnDefinition = "text")
    private String descricao;

    @Column(nullable = false)
    private String data;

    private String localizacao;

    private String fotoNomeArquivo;

    private String fotoContentType;

    @Column(columnDefinition = "bytea")
    @JsonIgnore
    private byte[] fotoDados;

    private String foto2NomeArquivo;

    private String foto2ContentType;

    @Column(columnDefinition = "bytea")
    @JsonIgnore
    private byte[] foto2Dados;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    @JsonIgnore
    private Professor professor;

    public boolean isTemFoto() {
        return fotoDados != null && fotoDados.length > 0;
    }

    public boolean isTemFoto2() {
        return foto2Dados != null && foto2Dados.length > 0;
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
