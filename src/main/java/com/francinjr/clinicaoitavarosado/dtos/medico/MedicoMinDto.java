package com.francinjr.clinicaoitavarosado.dtos.medico;

import com.francinjr.clinicaoitavarosado.dtos.endereco.CreateEnderecoDto;
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
public class MedicoMinDto {

	@NotNull(message = "O id não pode estar vázio")
	private Long id;
	
    private CreateEnderecoDto endereco;
    
    private PessoaMinDto pessoa;
    
}
