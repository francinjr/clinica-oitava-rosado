package com.francinjr.clinicaoitavarosado.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.francinjr.clinicaoitavarosado.entities.Endereco;
import com.francinjr.clinicaoitavarosado.exceptions.ResourceNotFoundException;
import com.francinjr.clinicaoitavarosado.repositories.EnderecoRepository;

@Service
public class EnderecoService {

	@Autowired
	private EnderecoRepository enderecoRepository;
	

	public Endereco findById(Long enderecoId) {
		Endereco entity = enderecoRepository.findById(enderecoId)
				.orElseThrow(() -> new ResourceNotFoundException("Endereço com id " + enderecoId 
						+ " não foi encontrado"));

		return entity;
	}

	
	@Transactional
	public Endereco create(Endereco endereco) {
		return enderecoRepository.save(endereco);
	}

	
	@Transactional
	public Endereco update(Endereco endereco) {
		Endereco entity = enderecoRepository.findById(endereco.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Endereço com id " 
						+ endereco.getId() + " não foi encontrado"));
		
		return enderecoRepository.save(endereco);
	}
	
	
	public void delete(Long enderecoId) {
		Endereco entity = enderecoRepository.findById(enderecoId)
				.orElseThrow(() -> new ResourceNotFoundException("Não foi possível deletar, "
						+ "não existe um endereco com id " + enderecoId));

		enderecoRepository.delete(entity);
	}
	
	

}
