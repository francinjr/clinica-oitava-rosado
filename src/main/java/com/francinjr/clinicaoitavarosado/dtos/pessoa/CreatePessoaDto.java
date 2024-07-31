package com.francinjr.clinicaoitavarosado.dtos.pessoa;

import com.francinjr.clinicaoitavarosado.dtos.endereco.CreateEnderecoDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class CreatePessoaDto {

    @NotBlank(message = "O nome completo não pode ser nulo")
    @Size(min = 1, max = 255, message = "O nome completo deve ter entre 1 e 255 caracteres")
    private String nomeCompleto;

    @NotBlank(message = "O CPF não pode ser nulo")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
    private String cpf;


    private CreateEnderecoDto endereco;

    @NotBlank(message = "O telefone não pode ser vázio")
    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos numéricos")
    private String telefone;

    @NotBlank(message = "O email não pode ser nulo")
    @Email(message = "O email deve ser um endereço de email válido")
    private String email;
}
