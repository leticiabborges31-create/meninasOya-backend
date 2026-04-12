package com.projeto.meninas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

    private Long idProfessor;

    @Column(nullable = false)
    private String data;

    private String fotoNomeArquivo;

    private String fotoContentType;

    @Lob
    @JsonIgnore
    private byte[] fotoDados;

    public boolean isTemFoto() {
        return fotoDados != null && fotoDados.length > 0;
    }
}
