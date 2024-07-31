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
public class ResetPasswordRequestDto {

    @NotBlank(message = "O email não pode ser vazio")
    @Email(message = "O email deve ser um endereço de email válido")
    private String email;

    @NotBlank(message = "A senha não pode ser vazia")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    private String password;

    @NotBlank(message = "A confirmação de senha não pode ser vazia")
    @Size(min = 8, message = "A confirmação de senha deve ter pelo menos 8 caracteres")
    private String passwordConfirmation;

    @NotBlank(message = "O código de verificação não pode ser vazio")
    private String verificationCode;

}
