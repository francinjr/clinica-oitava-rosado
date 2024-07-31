package com.francinjr.clinicaoitavarosado.dtos.endereco;

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
public class CreateEnderecoDto {

    @NotNull(message = "O logradouro não pode ser nulo")
    @Size(min = 1, max = 255, message = "O logradouro deve ter entre 1 e 255 caracteres")
    private String logradouro;

    @NotNull(message = "O bairro não pode ser nulo")
    @Size(min = 1, max = 100, message = "O bairro deve ter entre 1 e 100 caracteres")
    private String bairro;

    @NotNull(message = "A cidade não pode ser nula")
    @Size(min = 1, max = 100, message = "A cidade deve ter entre 1 e 100 caracteres")
    private String cidade;

    @NotNull(message = "A UF não pode ser nula")
    @Size(min = 2, max = 2, message = "A UF deve ter exatamente 2 caracteres")
    @Pattern(regexp = "[A-Z]{2}", message = "A UF deve conter apenas letras maiúsculas")
    private String uf;

    @NotNull(message = "O CEP não pode ser nulo")
    @Size(min = 8, max = 8, message = "O CEP deve ter exatamente 8 caracteres")
    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter apenas dígitos numéricos")
    private String cep;
}
