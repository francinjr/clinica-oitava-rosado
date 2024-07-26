package com.francinjr.clinicaoitavarosado.dtos.user;

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
public class CreateUsuarioDto {
    private String nome;
    private String email;
    private String senha;
}
