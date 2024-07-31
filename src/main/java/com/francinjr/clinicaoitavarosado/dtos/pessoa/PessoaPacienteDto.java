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
public class PessoaPacienteDto {
	// Os Dtos contém somente os dados necessários para as tabelas, mas poderia enviar 
	// todos os dados também, sem problema.
	private Long id;
    private String nomeCompleto;

    private String cpf;
   
    private String telefone;   
}
