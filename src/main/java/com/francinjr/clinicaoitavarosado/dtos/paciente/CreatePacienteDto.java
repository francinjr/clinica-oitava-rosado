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
public class CreatePacienteDto implements Serializable {
	private static final long serialVersionUID = 1L;

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
