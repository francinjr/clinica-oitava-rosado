package com.francinjr.clinicaoitavarosado.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import com.francinjr.clinicaoitavarosado.controllers.MedicoController;
import com.francinjr.clinicaoitavarosado.dtos.medico.CreateMedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.medico.MedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.medico.UpdateMedicoDto;
import com.francinjr.clinicaoitavarosado.entities.Medico;
import com.francinjr.clinicaoitavarosado.exceptions.ResourceNotFoundException;
import com.francinjr.clinicaoitavarosado.exceptions.UniqueConstraintViolationException;
import com.francinjr.clinicaoitavarosado.mappers.Mapper;
import com.francinjr.clinicaoitavarosado.repositories.MedicoRepository;

@Service
public class MedicoService {

	@Autowired
	private MedicoRepository medicoRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
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
				.orElseThrow(() -> new ResourceNotFoundException("Médico com id " + medicoId 
						+ " não foi encontrado"));

		MedicoDto dto = Mapper.parseObject(entity, MedicoDto.class);

		dto.add(linkTo(methodOn(MedicoController.class).findById(medicoId)).withSelfRel());
		return dto;
	}

	
	@Transactional
	public MedicoDto create(CreateMedicoDto medico) throws UniqueConstraintViolationException {
		Medico entity = Mapper.parseObject(medico, Medico.class);

		List<FieldError> camposInvalidos = validateDoctorUniqueFields(entity);
		if (!camposInvalidos.isEmpty()) {
			throw new UniqueConstraintViolationException("Há campos que são únicos já cadastrados", camposInvalidos);
		}

		MedicoDto dto = Mapper.parseObject(medicoRepository.save(entity), MedicoDto.class);
		dto.add(linkTo(methodOn(MedicoController.class).findById(dto.getKey())).withSelfRel());
		return dto;
	}

	public MedicoDto update(UpdateMedicoDto medico) 
			throws UniqueConstraintViolationException {
		// Só precisa saber se existe, para continuar ou não, executando as regras de
		// negócio
		Medico medicoEncontrado = medicoRepository.findById(medico.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Médico com id " + medico.getId() + " não foi encontrado"));

		Medico entity = Mapper.parseObject(medico, Medico.class);

		List<FieldError> camposInvalidos = validateDoctorUniqueFields(entity);

		if(!entity.getId().equals(medicoEncontrado.getId())) {
			if (!camposInvalidos.isEmpty()) {
				throw new UniqueConstraintViolationException("Há campos que são únicos já cadastrados", camposInvalidos);
			}
		}
		
		try {
			Medico medicoAtualizado = medicoRepository.save(entity);
			MedicoDto dto = Mapper.parseObject(medicoAtualizado, MedicoDto.class);
			dto.add(linkTo(methodOn(MedicoController.class).findById(dto.getKey())).withSelfRel());
			return dto;

		} catch(DataIntegrityViolationException exception) {
			throw new UniqueConstraintViolationException("Há campos que são únicos já "
					+ "cadastrados", camposInvalidos);
		}
	}
	
	
	public void delete(Long medicoId) {
		Medico entity = medicoRepository.findById(medicoId)
				.orElseThrow(() -> new ResourceNotFoundException("Não foi possível deletar, "
						+ "não existe um médico com id " + medicoId));

		medicoRepository.delete(entity);
	}
	
	
	private List<FieldError> validateDoctorUniqueFields(Medico medico) {
		List<FieldError> camposInvalidos = pessoaService
				.validatePersonUniqueFields(medico.getPessoa());

		return camposInvalidos;
	}
}
