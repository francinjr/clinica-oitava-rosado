package com.francinjr.clinicaoitavarosado.dtos.medico;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class MedicoDto extends RepresentationModel<MedicoDto> implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	
    private String nomeCompleto;
    private String conselhoMedico;

    private String ufConselho;
    
    private Integer numeroConselho;
    
    private String cbo;
    
    private String cpf;
    
    private String logradouro;

    private String bairro;

    private String cidade;

    private String uf;

    private String cep;
    
    private String telefone;
    
    private String email;


}
