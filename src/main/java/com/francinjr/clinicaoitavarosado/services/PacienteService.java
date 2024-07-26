package com.francinjr.clinicaoitavarosado.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.francinjr.clinicaoitavarosado.controllers.PacienteController;
import com.francinjr.clinicaoitavarosado.dtos.paciente.CreatePacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.PacienteDto;
import com.francinjr.clinicaoitavarosado.entities.Paciente;
import com.francinjr.clinicaoitavarosado.exceptions.ResourceAlreadyExistsException;
import com.francinjr.clinicaoitavarosado.exceptions.ResourceNotFoundException;
import com.francinjr.clinicaoitavarosado.mappers.Mapper;
import com.francinjr.clinicaoitavarosado.repositories.PacienteRepository;

@Service
public class PacienteService {

	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Autowired
	PagedResourcesAssembler<PacienteDto> assembler;

	public PagedModel<EntityModel<PacienteDto>> findAll(Pageable pageable) {
		
		var pacientePage = pacienteRepository.findAll(pageable);
		
		var pacienteDtosPage = pacientePage.map(p -> Mapper.parseObject(p, PacienteDto.class));
		
		pacienteDtosPage.map(
				p -> p.add(
						linkTo(methodOn(PacienteController.class)
								.findById(p.getKey())).withSelfRel()));
		
		Link link = linkTo(
				methodOn(PacienteController.class)
					.findAll(pageable.getPageNumber(),
							pageable.getPageSize(),
							"asc")).withSelfRel();
		return assembler.toModel(pacienteDtosPage, link);
	}


	public PacienteDto findById(Long pacienteId) {
		Paciente entity = pacienteRepository.findById(pacienteId)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso com id " + pacienteId 
						+ " não foi encontrado"));

		PacienteDto dto = Mapper.parseObject(entity, PacienteDto.class);

		dto.add(linkTo(methodOn(PacienteController.class).findById(pacienteId)).withSelfRel());
		return dto;
	}

	
	public PacienteDto create(CreatePacienteDto paciente) {
		Paciente pacientExists = pacienteRepository.findByCpf(paciente.getCpf());

		if(pacientExists != null) {
			throw new ResourceAlreadyExistsException("Já existe um paciente cadastrado com "
					+ "o CPF: " + paciente.getCpf());
		}
		
		Paciente entity = Mapper.parseObject(paciente, Paciente.class);

		PacienteDto dto = Mapper.parseObject(pacienteRepository.save(entity), PacienteDto.class);
		dto.add(linkTo(methodOn(PacienteController.class).findById(dto.getKey())).withSelfRel());
		return dto;
	}

	
	public PacienteDto update(PacienteDto paciente) {
		Paciente entity = pacienteRepository.findById(paciente.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Não foi possível atualizar, "
						+ "não existe um paciente com id " + paciente.getKey()));
		
		
		Paciente pacienteEncontrado = pacienteRepository.findByCpf(paciente.getCpf());

		// Impede que alguem atualize um CPF para um que pertence a outro paciente
		if(pacienteEncontrado != null && (paciente.getCpf().equals(pacienteEncontrado.getCpf()) 
				&& (!paciente.getKey().equals(pacienteEncontrado.getId())))) {
			throw new ResourceAlreadyExistsException("Já existe um paciente cadastrado com "
					+ "o CPF: " + paciente.getCpf());
		}
		


		entity = Mapper.parseObject(paciente, Paciente.class);

		PacienteDto dto = Mapper.parseObject(pacienteRepository.save(entity), PacienteDto.class);
		dto.add(linkTo(methodOn(PacienteController.class).findById(dto.getKey())).withSelfRel());
		return dto;
	}
	
	
	public void delete(Long pacienteId) {
		Paciente entity = pacienteRepository.findById(pacienteId)
				.orElseThrow(() -> new ResourceNotFoundException("Não foi possível deletar, "
						+ "já existe um paciente com id " + pacienteId));

		pacienteRepository.delete(entity);
	}
}
