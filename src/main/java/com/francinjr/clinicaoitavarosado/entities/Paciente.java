package com.francinjr.clinicaoitavarosado.entities;

import java.time.LocalDate;

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

@Entity(name = "pacientes")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Paciente {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    @Column(nullable = false)
    private String nomeCompleto;
	
    @Column(nullable = false)
    private String sexo;
    
    @Column(nullable = false)
    private LocalDate dataNascimento;
    
    @Column(length = 11, nullable = false, unique = true)
    private String cpf;
    
    @Column(nullable = false, unique = true)
    private String rg;
    
    @Column(nullable = false)
    private String orgaoEmissor;

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

    private String observacoes;
}
