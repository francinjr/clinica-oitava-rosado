package com.francinjr.clinicaoitavarosado.dtos.pessoa;

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
public class PessoaMedicoDto {
	private Long id;
    private String nomeCompleto;
    private String cpf;
}

