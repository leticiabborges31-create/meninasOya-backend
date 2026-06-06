package com.projeto.meninas.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "escolas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Escola implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "Tipo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoEscola tipo;

    @NotNull(message = "Cidade é obrigatória")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;

    @Column
    private String emailContato;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NivelEducacional nivelEducacional;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NaturezaJuridica natureza;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EsferaAdministrativa esferaAdministrativa;
}
