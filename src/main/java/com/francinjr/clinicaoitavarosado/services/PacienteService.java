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

import com.francinjr.clinicaoitavarosado.controllers.PacienteController;
import com.francinjr.clinicaoitavarosado.dtos.paciente.BasePacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.CreatePacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.PacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.UpdatePacienteDto;
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

	
	public PacienteDto create(CreatePacienteDto paciente) throws ResourceAlreadyExistsException {
		
		List<FieldError> camposInvalidos = validatePatientUniqueFields(paciente);
		if(camposInvalidos != null) {
			throw new ResourceAlreadyExistsException("Há campos que são únicos já cadastrados",
					camposInvalidos);
		}
		
		Paciente entity = Mapper.parseObject(paciente, Paciente.class);

		PacienteDto dto = Mapper.parseObject(pacienteRepository.save(entity), PacienteDto.class);
		dto.add(linkTo(methodOn(PacienteController.class).findById(dto.getKey())).withSelfRel());
		return dto;
	}

	
	public PacienteDto update(UpdatePacienteDto paciente) throws ResourceAlreadyExistsException {
		// Só precisa saber se existe, para continuar ou não, executando as regras de negócio
		Paciente pacienteEncontrado = pacienteRepository.findById(paciente.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Recurso com id " + paciente.getId() 
						+ " não foi encontrado"));
		

		List<FieldError> camposInvalidos = validatePatientUniqueFields(paciente);
		if(camposInvalidos != null) {
			// Impede que um usuário atualize para valores de campos únicos pertencentes a outro
			// paciente
			if(!paciente.getId().equals(pacienteEncontrado.getId())) {
				throw new ResourceAlreadyExistsException("Há campos inválidos", camposInvalidos);
			}
		}
		
		Paciente entity = Mapper.parseObject(paciente, Paciente.class);

		PacienteDto dto = Mapper.parseObject(pacienteRepository.save(entity), PacienteDto.class);
		dto.add(linkTo(methodOn(PacienteController.class).findById(dto.getKey())).withSelfRel());
		return dto;
	}
	
	
	public void delete(Long pacienteId) {
		Paciente entity = pacienteRepository.findById(pacienteId)
				.orElseThrow(() -> new ResourceNotFoundException("Não foi possível deletar, "
						+ "não existe um paciente com id " + pacienteId));

		pacienteRepository.delete(entity);
	}
	
	
	private List<FieldError> validatePatientUniqueFields(BasePacienteDto paciente) 
			throws ResourceAlreadyExistsException {
		Paciente pacienteEncontrado = pacienteRepository.findByCpfOrRgOrTelefoneOrEmail(
				paciente.getCpf(), paciente.getRg(), paciente.getTelefone(), 
					paciente.getEmail());
		
		
		if(pacienteEncontrado != null) {
			boolean haCampoInvalido = false;
			
			List<FieldError> camposInvalidos = new ArrayList<>();
			
			if(paciente.getCpf().equals(pacienteEncontrado.getCpf())) {
				camposInvalidos.add(new FieldError("paciente", "cpf", "Já existe um paciente"
						+ " cadastrado com o CPF: " + paciente.getCpf()));
				haCampoInvalido = true;
			}
			
			if(paciente.getRg().equals(pacienteEncontrado.getRg())) {
				camposInvalidos.add(new FieldError("paciente", "rg", "Já existe um paciente"
						+ " cadastrado com o RG: " + paciente.getRg()));
				haCampoInvalido = true;
			}
			
			if(paciente.getTelefone().equals(pacienteEncontrado.getTelefone())) {
				camposInvalidos.add(new FieldError("paciente", "telefone", "Já existe um paciente"
						+ " cadastrado com o Telefone: " + paciente.getTelefone()));
				haCampoInvalido = true;
			}
			
			if(paciente.getEmail().equals(pacienteEncontrado.getEmail())) {
				camposInvalidos.add(new FieldError("paciente", "email", "Já existe um paciente"
						+ " cadastrado com o Email: " + paciente.getEmail()));
				haCampoInvalido = true;
			}
			
			if(haCampoInvalido) {
				return camposInvalidos;
			}
		}
		return null;
	}
}
