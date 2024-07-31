package com.francinjr.clinicaoitavarosado.dtos.paciente;

import java.time.LocalDate;

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
public class CreatePacienteDto {

	@Valid
    @NotNull(message = "A pessoa não pode ser nula")
    private CreatePessoaDto pessoa;
    
    @NotBlank(message = "O sexo não pode ser vazio")
    @Pattern(regexp = "^(M|F)$", message = "O sexo deve ser 'M' para masculino ou 'F' para feminino")
    private String sexo;
    
    @NotNull(message = "A data de nascimento não pode ser nula")
    private LocalDate dataNascimento;
    
    @NotBlank(message = "O RG não pode ser vazio")
    @Pattern(regexp = "\\d{9}", message = "O RG deve conter exatamente 9 dígitos numéricos")
    private String rg;
    
    @NotBlank(message = "O órgão emissor não pode ser vazio")
    @Size(min = 1, max = 50, message = "O órgão emissor deve ter entre 1 e 50 caracteres")
    private String orgaoEmissor;

    @Size(max = 500, message = "Observações não podem ter mais de 500 caracteres")
    private String observacoes;
}
