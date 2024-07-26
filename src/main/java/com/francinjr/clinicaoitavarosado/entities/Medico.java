package com.francinjr.clinicaoitavarosado.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "medicos")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Medico {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false)
    private String conselhoMedico;

    @Column(nullable = false)
    private String ufConselho;
    
    @Column(nullable = false)
    private Integer numeroConselho;
    
    private String cbo;
    
    @Column(length = 11, nullable = false, unique = true)
    private String cpf;
    
    @Column(nullable = false)
    private String logradouro;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String uf;

    @Column(nullable = false)
    private String cep;
    
    @Column(nullable = false, unique = true)
    private String telefone;
    
    @Column(nullable = false, unique = true)
    private String email;


}
