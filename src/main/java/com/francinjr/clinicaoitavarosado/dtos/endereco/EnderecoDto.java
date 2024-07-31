package com.francinjr.clinicaoitavarosado.dtos.endereco;

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
public class EnderecoDto {
	private Long id;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
}
