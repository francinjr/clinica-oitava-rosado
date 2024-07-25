package com.francinjr.clinicaoitavarosado.entities;


import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeCompleto;

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
