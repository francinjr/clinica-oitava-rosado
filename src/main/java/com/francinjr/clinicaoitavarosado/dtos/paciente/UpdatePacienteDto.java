package com.francinjr.clinicaoitavarosado.dtos.paciente;

import java.io.Serializable;
import java.time.LocalDate;

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
public class UpdatePacienteDto implements Serializable, BasePacienteDto {
	private static final long serialVersionUID = 1L;

	/* Pode atualizar todos os dados de um paciente, mesmo os que não estão aparecendo na 
	 * tabela no figma, visto que futuramente pode haver no frontend uma tela que mostra 
	 * todos os dados, disponibilizando-os para atualização */
	
	private Long id;
    private String nomeCompleto;
    private String sexo;
    private LocalDate dataNascimento;
    private String cpf;
    private String rg;
    private String orgaoEmissor;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String telefone;
    private String email;
    private String observacoes;
}
