package com.francinjr.clinicaoitavarosado.dtos.paciente;

import com.francinjr.clinicaoitavarosado.dtos.pessoa.PessoaMinDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class PacienteMinDto {

	
	@NotNull(message = "O id não pode estar vázio")
	private Long id;
	
	@Valid
	@NotNull(message = "A pessoa não pode estar nula")
    private PessoaMinDto pessoa;

}
