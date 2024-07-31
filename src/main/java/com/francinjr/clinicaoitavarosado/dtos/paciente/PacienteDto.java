package com.francinjr.clinicaoitavarosado.dtos.paciente;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.francinjr.clinicaoitavarosado.dtos.pessoa.PessoaPacienteDto;

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
public class PacienteDto extends RepresentationModel<PacienteDto> implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
    private Long key;
    
    private PessoaPacienteDto pessoa;
	
    private String sexo;
    
    private LocalDate dataNascimento;
    private String rg;
    private String observacoes;
}
