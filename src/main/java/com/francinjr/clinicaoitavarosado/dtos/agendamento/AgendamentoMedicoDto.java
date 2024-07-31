package com.francinjr.clinicaoitavarosado.dtos.agendamento;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.francinjr.clinicaoitavarosado.dtos.medico.MedicoMinDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.PacienteMinDto;

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
public class AgendamentoMedicoDto extends RepresentationModel<AgendamentoMedicoDto> implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	
	// Estou usando MedicoMinDto e PacienteMinDto porque observando o figma, são só esses dados
	// que são necessários para a tabela de agendamentos. Esses dtos também servem para 
	// impedir que quando um agendamento seja feito os dados do medico ou paciente sejam
	// comprometidos. Esses dtos também reduzem o tamanho do body nas requisições.
    private MedicoMinDto medico;
    private PacienteMinDto paciente;
    //private String motivoConsulta;
    private LocalDate dataConsulta;
    private LocalTime inicio;
    private LocalTime fim;
    //private String local;
    private String observacoes;
}
