package com.francinjr.clinicaoitavarosado.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.francinjr.clinicaoitavarosado.entities.Pessoa;
import com.francinjr.clinicaoitavarosado.exceptions.UniqueConstraintViolationException;
import com.francinjr.clinicaoitavarosado.repositories.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public List<FieldError> validatePersonUniqueFields(Pessoa pessoa) throws UniqueConstraintViolationException {
		List<FieldError> camposInvalidos = new ArrayList<>();
		boolean cpfExiste = pessoaRepository.existsByCpf(pessoa.getCpf());
		if (cpfExiste) {
			camposInvalidos.add(new FieldError("pessoa", "cpf",
					"Já existe uma pessoa" + " cadastrado com o CPF: " + pessoa.getCpf()));
		}

		boolean telefoneExiste = pessoaRepository.existsByTelefone(pessoa.getTelefone());
		if (telefoneExiste) {
			camposInvalidos.add(new FieldError("pessoa", "telefone", "Já existe uma "
					+ "pessoa cadastrada com o Telefone: " + pessoa.getTelefone()));
		}

		boolean emailExiste = pessoaRepository.existsByEmail(pessoa.getEmail());
		if (emailExiste) {
			camposInvalidos.add(new FieldError("pessoa", "email",
					"Já existe uma pessoa" + " cadastrado com o Email: " + pessoa.getEmail()));
		}
		return camposInvalidos;
	}

}
