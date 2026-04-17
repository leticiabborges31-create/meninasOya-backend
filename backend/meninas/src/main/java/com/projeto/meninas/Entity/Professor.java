package com.projeto.meninas.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "professores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professor implements Serializable {
    //transforma professor em bytes

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @Min(value = 0, message = "Idade inválida")
    @Max(value = 150, message = "Idade inválida")
    @Column(nullable = false)
    private Integer idade;

    @NotBlank(message = "UF é obrigatória")
    @Size(min = 2, max = 2, message = "UF deve ser a sigla com 2 letras")
    @Column(nullable = false, length = 2)
    private String uf;

    @NotBlank(message = "Escola é obrigatória")
    @Column(nullable = false)
    private String escola;

    @NotBlank(message = "Link do currículo Lattes é obrigatório")
    @URL(message = "Link do currículo Lattes deve ser uma URL válida")
    @Column(nullable = false)
    private String linkCurriculoLattes;
}

