package com.francinjr.clinicaoitavarosado.dtos.paciente;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
@JsonPropertyOrder({"id", "nomeCompleto", "sexo", "dataNascimento", "cpf", "rg", 
	"orgaoEmissor", "logradouro", "bairro", "cidade", "uf", "cep", "telefone", "email", 
	"observacoes"})
public class PacienteDto extends RepresentationModel<PacienteDto> implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
    private String nomeCompleto;
    private String cpf;
    private String telefone;
    private String sexo;
    private LocalDate dataNascimento;
    private String observacoes;
    
    
    /*private String rg;
    private String orgaoEmissor;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String email;*/


}
