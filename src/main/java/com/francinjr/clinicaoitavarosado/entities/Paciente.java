package com.francinjr.clinicaoitavarosado.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "pacientes")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Paciente extends User {

    @Column(nullable = false)
    private String sexo;

    @Column(nullable = false)
    private LocalDate dataDeNascimento;

    @Column(nullable = false, unique = true)
    private String RG;

    @Column(nullable = false)
    private String orgaoEmissor;

    private String observacoes;
}
