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

import com.francinjr.clinicaoitavarosado.controllers.MedicoController;
import com.francinjr.clinicaoitavarosado.controllers.PacienteController;
import com.francinjr.clinicaoitavarosado.dtos.medico.CreateMedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.medico.MedicoDto;
import com.francinjr.clinicaoitavarosado.entities.Medico;
import com.francinjr.clinicaoitavarosado.exceptions.ResourceAlreadyExistsException;
import com.francinjr.clinicaoitavarosado.exceptions.ResourceNotFoundException;
import com.francinjr.clinicaoitavarosado.mappers.Mapper;
import com.francinjr.clinicaoitavarosado.repositories.MedicoRepository;

@Service
public class MedicoService {

	@Autowired
	private MedicoRepository medicoRepository;
	
	@Autowired
	PagedResourcesAssembler<MedicoDto> assembler;

	public PagedModel<EntityModel<MedicoDto>> findAll(Pageable pageable) {
		
		var medicoPage = medicoRepository.findAll(pageable);
		
		var medicoDtosPage = medicoPage.map(p -> Mapper.parseObject(p, MedicoDto.class));
		
		medicoDtosPage.map(
				m -> m.add(
						linkTo(methodOn(MedicoController.class)
								.findById(m.getKey())).withSelfRel()));
		
		Link link = linkTo(
				methodOn(MedicoController.class)
					.findAll(pageable.getPageNumber(),
							pageable.getPageSize(),
							"asc")).withSelfRel();
		return assembler.toModel(medicoDtosPage, link);
	}


	public MedicoDto findById(Long medicoId) {
		Medico entity = medicoRepository.findById(medicoId)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso com id " + medicoId 
						+ " não foi encontrado"));

		MedicoDto dto = Mapper.parseObject(entity, MedicoDto.class);

		dto.add(linkTo(methodOn(MedicoController.class).findById(medicoId)).withSelfRel());
		return dto;
	}

	
	public MedicoDto create(CreateMedicoDto medico) {
		Medico medicoEncontrado = medicoRepository.findByCpf(medico.getCpf());

		if(medicoEncontrado != null) {
			throw new ResourceAlreadyExistsException("Já existe um médico cadastrado com "
					+ "o CPF: " + medico.getCpf());
		}
		
		Medico entity = Mapper.parseObject(medico, Medico.class);

		MedicoDto dto = Mapper.parseObject(medicoRepository.save(entity), MedicoDto.class);
		dto.add(linkTo(methodOn(PacienteController.class).findById(dto.getKey())).withSelfRel());
		return dto;
	}

	
	public MedicoDto update(MedicoDto medico) {
		Medico entity = medicoRepository.findById(medico.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Não foi possível atualizar, "
						+ "não existe um médico com id " + medico.getKey()));
		
		
		Medico medicoEncontrado = medicoRepository.findByCpf(medico.getCpf());

		// Impede que alguem atualize um CPF para um que pertence a outro médico
		if(medicoEncontrado != null && (medico.getCpf().equals(medicoEncontrado.getCpf()) 
				&& (!medico.getKey().equals(medicoEncontrado.getId())))) {
			throw new ResourceAlreadyExistsException("Não foi possível atualizar, já existe "
					+ "um médico cadastrado com o CPF: " + medico.getCpf());
		}
		


		entity = Mapper.parseObject(medico, Medico.class);

		MedicoDto dto = Mapper.parseObject(medicoRepository.save(entity), MedicoDto.class);
		dto.add(linkTo(methodOn(PacienteController.class).findById(dto.getKey())).withSelfRel());
		return dto;
	}
	
	
	public void delete(Long medicoId) {
		Medico entity = medicoRepository.findById(medicoId)
				.orElseThrow(() -> new ResourceNotFoundException("Não foi possível deletar, "
						+ "já existe um médico com id " + medicoId));

		medicoRepository.delete(entity);
	}
}
