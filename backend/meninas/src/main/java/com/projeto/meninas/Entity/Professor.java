package com.projeto.meninas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "professores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email e obrigatorio")
    @Email(message = "Email invalido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Nome e obrigatorio")
    @Column(nullable = false)
    private String nome;

    @Min(value = 0, message = "Idade invalida")
    @Max(value = 150, message = "Idade invalida")
    @Column(nullable = false)
    private Integer idade;

    @NotBlank(message = "UF e obrigatoria")
    @Size(min = 2, max = 2, message = "UF deve ser a sigla com 2 letras")
    @Column(nullable = false, length = 2)
    private String uf;

    @NotBlank(message = "Escola e obrigatoria")
    @Column(nullable = false)
    private String escola;

    @NotBlank(message = "Link do curriculo Lattes e obrigatorio")
    @Pattern(regexp = "https?://.*", message = "Link do curriculo Lattes deve ser uma URL valida")
    @Column(nullable = false)
    private String linkCurriculoLattes;

    @JsonIgnore
    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
}
