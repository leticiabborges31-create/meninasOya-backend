package com.projeto.meninas.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome e obrigatorio")
    @Column(nullable = false)
    private String nome;

    @Min(value = 0, message = "Idade invalida")
    @Max(value = 150, message = "Idade invalida")
    @Column(nullable = false)
    private Integer idade;

    @NotBlank(message = "UF e obrigatoria")
    @Size(min = 2, max = 2, message = "UF deve ser a sigla com 2 letras")
    @Column(name = "uf", nullable = false, length = 2)
    private String uf;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "escola_id")
    private Escola escola;

    public String getEstado() {
        return uf;
    }

    public void setEstado(String estado) {
        this.uf = estado;
    }
}
