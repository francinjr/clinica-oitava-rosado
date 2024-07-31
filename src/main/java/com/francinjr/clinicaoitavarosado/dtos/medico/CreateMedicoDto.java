package com.francinjr.clinicaoitavarosado.dtos.medico;

import com.francinjr.clinicaoitavarosado.dtos.pessoa.CreatePessoaDto;

import jakarta.validation.Valid;
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
public class CreateMedicoDto {

	@Valid
    @NotNull(message = "A pessoa não pode ser nula")
    private CreatePessoaDto pessoa;

    @NotNull(message = "O conselho médico não pode ser nulo")
    @Size(min = 1, max = 50, message = "O conselho médico deve ter entre 1 e 50 caracteres")
    private String conselhoMedico;

    @NotNull(message = "A UF do conselho não pode ser nula")
    @Size(min = 2, max = 2, message = "A UF do conselho deve ter exatamente 2 caracteres")
    @Pattern(regexp = "[A-Z]{2}", message = "A UF do conselho deve conter apenas letras maiúsculas")
    private String ufConselho;

    @NotBlank(message = "O número do conselho não pode ser vazia")
    @Size(min = 4, max = 6, message = "A UF do conselho deve ter de 4 até 6 dígitos")
    private String numeroConselho;

    @NotNull(message = "O CBO não pode ser nulo")
    @Size(min = 8, max = 8, message = "O CBO deve ter exatamente 8 caracteres")
    @Pattern(regexp = "\\d{8}", message = "O CBO deve conter exatamente 8 dígitos numéricos")
    private String cbo;
}
