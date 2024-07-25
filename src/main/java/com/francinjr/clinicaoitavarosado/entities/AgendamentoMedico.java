package com.francinjr.clinicaoitavarosado.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "agendamentos")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AgendamentoMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(nullable = false)
    private String motivoDaConsulta;

    @Column(nullable = false)
    private LocalDate dataDaConsulta;

    @Column(nullable = false)
    private LocalDate horaDaConsulta;

    @Column(nullable = false)
    private String local;

    private String observacoes;
}
