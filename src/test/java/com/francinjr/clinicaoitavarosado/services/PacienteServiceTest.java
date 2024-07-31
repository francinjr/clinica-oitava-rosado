package com.francinjr.clinicaoitavarosado.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.validation.FieldError;

import com.francinjr.clinicaoitavarosado.dtos.paciente.CreatePacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.PacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.UpdatePacienteDto;
import com.francinjr.clinicaoitavarosado.entities.Paciente;
import com.francinjr.clinicaoitavarosado.entities.Pessoa;
import com.francinjr.clinicaoitavarosado.exceptions.ResourceNotFoundException;
import com.francinjr.clinicaoitavarosado.exceptions.UniqueConstraintViolationException;
import com.francinjr.clinicaoitavarosado.repositories.PacienteRepository;

public class PacienteServiceTest {

	@Mock
	private PacienteRepository pacienteRepository;

	@Mock
	private PessoaService pessoaService;

	@Mock
	private PagedResourcesAssembler<PacienteDto> assembler;

	@InjectMocks
	private PacienteService pacienteService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreatePacienteSuccess() throws UniqueConstraintViolationException {
		CreatePacienteDto createDto = new CreatePacienteDto();
		Paciente savedPaciente = new Paciente(1L, new Pessoa(), "F", LocalDate.now(), "123456789", "SSP", "Observação");

		when(pacienteRepository.save(any(Paciente.class))).thenReturn(savedPaciente);
		when(pessoaService.validatePersonUniqueFields(any())).thenReturn(new ArrayList<>());

		PacienteDto result = pacienteService.create(createDto);

		assertThat(result).isNotNull();
		assertThat(result.getKey()).isEqualTo(savedPaciente.getId());
		verify(pacienteRepository).save(any(Paciente.class));
	}

	@Test
	void testCreatePacienteUniqueConstraintViolation() {
		CreatePacienteDto createDto = new CreatePacienteDto();
		List<FieldError> fieldErrors = List.of(new FieldError("paciente", "rg", "RG já cadastrado"));

		when(pessoaService.validatePersonUniqueFields(any())).thenReturn(fieldErrors);

		UniqueConstraintViolationException thrown = assertThrows(UniqueConstraintViolationException.class, () -> {
			pacienteService.create(createDto);
		});

		assertThat(thrown.getCamposInvalidos()).isEqualTo(fieldErrors);
	}

	@Test
	void testFindByIdSuccess() {
		Paciente paciente = new Paciente(1L, new Pessoa(), "F", LocalDate.now(), "123456789", "SSP", "Observação");
		when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

		PacienteDto result = pacienteService.findById(1L);

		assertThat(result).isNotNull();
		assertThat(result.getKey()).isEqualTo(paciente.getId());
	}

	@Test
	void testFindByIdNotFound() {
		when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

		ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
			pacienteService.findById(1L);
		});

		assertThat(thrown.getMessage()).contains("Paciente com id 1 não foi encontrado");
	}

	@Test
	void testUpdatePacienteSuccess() throws UniqueConstraintViolationException {
		UpdatePacienteDto updateDto = new UpdatePacienteDto();
		updateDto.setId(1L);
		Paciente paciente = new Paciente(1L, new Pessoa(), "F", LocalDate.now(), "123456789", "SSP", "Observação");
		Paciente updatedPaciente = new Paciente(1L, new Pessoa(), "M", LocalDate.now(), "123456789", "SSP",
				"Observação Atualizada");

		when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
		when(pacienteRepository.save(any(Paciente.class))).thenReturn(updatedPaciente);
		when(pessoaService.validatePersonUniqueFields(any())).thenReturn(new ArrayList<>());

		PacienteDto result = pacienteService.update(updateDto);

		assertThat(result).isNotNull();
		assertThat(result.getKey()).isEqualTo(updatedPaciente.getId());
		assertThat(result.getSexo()).isEqualTo("M");
		verify(pacienteRepository).save(any(Paciente.class));
	}

	@Test
	void testUpdatePacienteNotFound() {
		UpdatePacienteDto updateDto = new UpdatePacienteDto();
		updateDto.setId(1L);

		when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

		ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
			pacienteService.update(updateDto);
		});

		assertThat(thrown.getMessage()).contains("Paciente com id 1 não foi encontrado");
	}

	@Test
	void testUpdatePacienteUniqueConstraintViolation() {
		// Dados do teste
		UpdatePacienteDto updateDto = new UpdatePacienteDto();
		updateDto.setId(1L);
		Paciente paciente = new Paciente(1L, new Pessoa(), "F", LocalDate.now(), "123456789", "SSP", "Observação");

		when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

		List<FieldError> camposInvalidos = List.of(new FieldError("paciente", "rg", "RG já cadastrado"));
		UniqueConstraintViolationException exception = new UniqueConstraintViolationException("RG já cadastrado",
				camposInvalidos);
		when(pessoaService.validatePersonUniqueFields(any())).thenThrow(exception);

		UniqueConstraintViolationException thrown = assertThrows(UniqueConstraintViolationException.class, () -> {
			pacienteService.update(updateDto);
		});

		assertThat(thrown.getExceptionMessage()).contains("RG já cadastrado");

		assertThat(thrown.getCamposInvalidos()).isNotEmpty();
		assertThat(thrown.getCamposInvalidos().get(0).getField()).isEqualTo("rg");
		assertThat(thrown.getCamposInvalidos().get(0).getDefaultMessage()).isEqualTo("RG já cadastrado");
	}

	@Test
	void testDeletePacienteSuccess() {
		Paciente paciente = new Paciente(1L, new Pessoa(), "F", LocalDate.now(), "123456789", "SSP", "Observação");

		when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

		pacienteService.delete(1L);

		verify(pacienteRepository).delete(paciente);
	}

	@Test
	void testDeletePacienteNotFound() {
		when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

		ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
			pacienteService.delete(1L);
		});

		assertThat(thrown.getMessage()).contains("Não foi possível deletar, não existe um paciente com id 1");
	}
}
