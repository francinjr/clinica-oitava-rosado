package com.francinjr.clinicaoitavarosado.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import com.francinjr.clinicaoitavarosado.controllers.PacienteController;
import com.francinjr.clinicaoitavarosado.dtos.paciente.CreatePacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.PacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.UpdatePacienteDto;
import com.francinjr.clinicaoitavarosado.entities.Paciente;
import com.francinjr.clinicaoitavarosado.exceptions.ResourceNotFoundException;
import com.francinjr.clinicaoitavarosado.exceptions.UniqueConstraintViolationException;
import com.francinjr.clinicaoitavarosado.mappers.Mapper;
import com.francinjr.clinicaoitavarosado.repositories.PacienteRepository;

@Service
public class PacienteService {

	private final PacienteRepository pacienteRepository;
	private final PessoaService pessoaService;
	private final PagedResourcesAssembler<PacienteDto> assembler;

	// Usando construtor para que esse service possa ser usado nos teste com Junit e
	// Mockito
	// @Autowired
	public PacienteService(PacienteRepository pacienteRepository, PessoaService pessoaService,
			PagedResourcesAssembler<PacienteDto> assembler) {
		this.pacienteRepository = pacienteRepository;
		this.pessoaService = pessoaService;
		this.assembler = assembler;
	}

	public PagedModel<EntityModel<PacienteDto>> findAll(Pageable pageable) {

		var pacientePage = pacienteRepository.findAll(pageable);

		var pacienteDtosPage = pacientePage.map(p -> Mapper.parseObject(p, PacienteDto.class));

		pacienteDtosPage.map(p -> p.add(linkTo(methodOn(PacienteController.class).findById(p.getKey())).withSelfRel()));

		Link link = linkTo(
				methodOn(PacienteController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(pacienteDtosPage, link);
	}

	public PacienteDto findById(Long pacienteId) {
		Paciente entity = pacienteRepository.findById(pacienteId).orElseThrow(
				() -> new ResourceNotFoundException("Paciente com id " + pacienteId + " não foi encontrado"));

		PacienteDto dto = Mapper.parseObject(entity, PacienteDto.class);

		dto.add(linkTo(methodOn(PacienteController.class).findById(pacienteId)).withSelfRel());
		return dto;
	}

	@Transactional
	public PacienteDto create(CreatePacienteDto paciente) throws UniqueConstraintViolationException {
		Paciente entity = Mapper.parseObject(paciente, Paciente.class);

		List<FieldError> camposInvalidos = validatePatientUniqueFields(entity);
		// Se tem campos inválidos em Pessoa ou Paciente, então eu lanço a exceção de
		// uma vez,e retorno todos os campos inválidos informando o motivo. Isso é
		// interessante
		// para a experiência do usuário.
		if (!camposInvalidos.isEmpty()) {
			throw new UniqueConstraintViolationException("Há campos que são únicos já cadastrados", camposInvalidos);
		}

		PacienteDto dto = Mapper.parseObject(pacienteRepository.save(entity), PacienteDto.class);
		dto.add(linkTo(methodOn(PacienteController.class).findById(dto.getKey())).withSelfRel());
		return dto;
	}

	public PacienteDto update(UpdatePacienteDto paciente) throws UniqueConstraintViolationException {
		// Só precisa saber se existe, para continuar ou não, executando as regras de
		// negócio
		Paciente pacienteEncontrado = pacienteRepository.findById(paciente.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Paciente com id " + paciente.getId() + " não foi encontrado"));

		Paciente entity = Mapper.parseObject(paciente, Paciente.class);

		List<FieldError> camposInvalidos = validatePatientUniqueFields(entity);

		// O motivo pelo qual não chamei o metodo validatePersonField no pacienteSercice
		// é que
		// se eu fizer isso ele não vai me retornar os campos que estão inválidos, e ele
		// já
		// lançaria a exception. Da forma como fiz, eu vou poder lançar a Exception uma
		// vez
		// e já retornar todos os campos inválidos para o Usuário, de uma única vez.
		if (!entity.getId().equals(pacienteEncontrado.getId())) {
			if (!camposInvalidos.isEmpty()) {
				throw new UniqueConstraintViolationException("Há campos que são únicos já cadastrados",
						camposInvalidos);
			}
		}
		
		try {
			Paciente pacienteAtualizado = pacienteRepository.save(entity);
			PacienteDto dto = Mapper.parseObject(pacienteAtualizado, PacienteDto.class);
			dto.add(linkTo(methodOn(PacienteController.class).findById(dto.getKey())).withSelfRel());
			return dto;

		} catch (DataIntegrityViolationException exception) {
			throw new UniqueConstraintViolationException("Há campos que são únicos já " 
					+ "cadastrados", camposInvalidos);
		}
	}

	public void delete(Long pacienteId) {
		Paciente entity = pacienteRepository.findById(pacienteId).orElseThrow(() -> new ResourceNotFoundException(
				"Não foi possível deletar, " + "não existe um paciente com id " + pacienteId));

		pacienteRepository.delete(entity);
	}

	private List<FieldError> validatePatientUniqueFields(Paciente paciente) throws UniqueConstraintViolationException {
		List<FieldError> camposInvalidos = pessoaService.validatePersonUniqueFields(paciente.getPessoa());

		boolean rgExiste = pacienteRepository.existsByRg(paciente.getRg());
		if (rgExiste) {
			camposInvalidos.add(new FieldError("paciente", "rg",
					"Já existe um paciente" + " cadastrado com o RG: " + paciente.getRg()));
		}

		return camposInvalidos;
	}

}
