package com.francinjr.clinicaoitavarosado.dtos.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class CreateUserDto {

    @NotBlank(message = "O nome de usuário não pode ser vazio")
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres")
    private String userName;

    @NotBlank(message = "O email não pode ser vazio")
    @Email(message = "O email deve ser um endereço de email válido")
    private String email;

    @NotBlank(message = "A senha não pode ser vazia")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    private String password;
}
