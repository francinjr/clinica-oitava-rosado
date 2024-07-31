package com.francinjr.clinicaoitavarosado.dtos.agendamento;

import java.time.LocalDate;
import java.time.LocalTime;

import com.francinjr.clinicaoitavarosado.dtos.medico.MedicoMinDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.PacienteMinDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CreateAgendamentoMedicoDto {
	@NotNull(message = "O médico não pode ser nulo")
    private MedicoMinDto medico;
	
	@NotNull(message = "O paciente não pode ser nulo")
    private PacienteMinDto paciente;
	
    @NotNull(message = "O motivo da consulta não pode ser nulo")
    @Size(min = 1, max = 255, message = "O motivo da consulta deve ter entre 1 e 255 caracteres")
    private String motivoConsulta;
    
    //@NotNull(message = "A data da consulta não pode ser nula")
    private LocalDate dataConsulta;
    
    @Schema(type = "String", pattern = "HH:mm")
    //@NotNull(message = "O horário de início não pode estar vázio")
    private LocalTime inicio;

    @Schema(type = "String", pattern = "HH:mm")
    //@NotNull(message = "O horário de fim não pode estar vázio")
    private LocalTime fim;

    @NotNull(message = "O local não pode ser nulo")
    @Size(min = 1, max = 255, message = "O local deve ter entre 1 e 255 caracteres")
    private String local;

    @Size(max = 500, message = "Observações não podem ter mais de 500 caracteres")
    private String observacoes;
}
