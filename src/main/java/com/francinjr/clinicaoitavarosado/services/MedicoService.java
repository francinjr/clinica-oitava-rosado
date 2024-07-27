package com.francinjr.clinicaoitavarosado.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.francinjr.clinicaoitavarosado.controllers.MedicoController;
import com.francinjr.clinicaoitavarosado.controllers.PacienteController;
import com.francinjr.clinicaoitavarosado.dtos.medico.BaseMedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.medico.CreateMedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.medico.MedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.medico.UpdateMedicoDto;
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

	
	public MedicoDto create(CreateMedicoDto medico) throws ResourceAlreadyExistsException {
		
		List<FieldError> camposInvalidos = validateDoctorUniqueFields(medico);
		if(camposInvalidos != null) {
			throw new ResourceAlreadyExistsException("Há campos que são únicos já cadastrados",
					camposInvalidos);
		}
		
		Medico entity = Mapper.parseObject(medico, Medico.class);

		MedicoDto dto = Mapper.parseObject(medicoRepository.save(entity), MedicoDto.class);
		dto.add(linkTo(methodOn(MedicoController.class).findById(dto.getKey())).withSelfRel());
		return dto;
	}

	
	public MedicoDto update(UpdateMedicoDto medico) throws ResourceAlreadyExistsException {
		Medico medicoEncontrado = medicoRepository.findById(medico.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Recurso com id " + medico.getId() 
						+ " não foi encontrado"));
		

		List<FieldError> camposInvalidos = validateDoctorUniqueFields(medico);
		if(camposInvalidos != null) {
			// Impede que um usuário atualize para valores de campos únicos pertencentes a outro
			// médico
			if(!medico.getId().equals(medicoEncontrado.getId())) {
				throw new ResourceAlreadyExistsException("Há campos inválidos", camposInvalidos);
			}
		}
		
		Medico entity = Mapper.parseObject(medico, Medico.class);

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
	
	
	private List<FieldError> validateDoctorUniqueFields(BaseMedicoDto medico) 
			throws ResourceAlreadyExistsException {
		Medico medicoEncontrado = medicoRepository.findByCpfOrTelefoneOrEmail(
				medico.getCpf(), medico.getTelefone(), medico.getEmail());
		
		
		if(medicoEncontrado != null) {
			boolean haCampoInvalido = false;
			
			List<FieldError> camposInvalidos = new ArrayList<>();
			
			if(medico.getCpf().equals(medicoEncontrado.getCpf())) {
				camposInvalidos.add(new FieldError("medico", "cpf", "Já existe um médico"
						+ " cadastrado com o CPF: " + medico.getCpf()));
				haCampoInvalido = true;
			}
			
			if(medico.getTelefone().equals(medicoEncontrado.getTelefone())) {
				camposInvalidos.add(new FieldError("medico", "telefone", "Já existe um médico"
						+ " cadastrado com o Telefone: " + medico.getTelefone()));
				haCampoInvalido = true;
			}
			
			if(medico.getEmail().equals(medicoEncontrado.getEmail())) {
				camposInvalidos.add(new FieldError("medico", "email", "Já existe um médico"
						+ " cadastrado com o Email: " + medico.getEmail()));
				haCampoInvalido = true;
			}
			
			if(haCampoInvalido) {
				return camposInvalidos;
			}
		}
		return null;
	}
}
