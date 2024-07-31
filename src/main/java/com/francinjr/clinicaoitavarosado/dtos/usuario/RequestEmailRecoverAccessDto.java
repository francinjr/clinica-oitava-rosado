package com.francinjr.clinicaoitavarosado.dtos.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class RequestEmailRecoverAccessDto {
    @NotBlank(message = "O email não pode ser vazio")
    @Email(message = "O email deve ser um endereço de email válido")
    private String email;
}
