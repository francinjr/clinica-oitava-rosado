package com.francinjr.clinicaoitavarosado.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "medicos")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Medico extends User {

    @Column(nullable = false)
    private String conselhoMedico;

    @Column(nullable = false)
    private String UFConselho;

    @Column(nullable = false)
    private Integer numeroDoConselho;

    private String CBO;
}
