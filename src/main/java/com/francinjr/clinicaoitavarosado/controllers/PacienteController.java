package com.francinjr.clinicaoitavarosado.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.francinjr.clinicaoitavarosado.dtos.paciente.CreatePacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.PacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.UpdatePacienteDto;
import com.francinjr.clinicaoitavarosado.services.PacienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pacientes/v1")
public class PacienteController {

	@Autowired
	PacienteService pacienteService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedModel<EntityModel<PacienteDto>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "10") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
			) {
		
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nomeCompleto"));
		
		return ResponseEntity.ok(pacienteService.findAll(pageable));
	}


	@GetMapping(value = "/{pacienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PacienteDto> findById(@PathVariable Long pacienteId) {
		PacienteDto pacienteDto = pacienteService.findById(pacienteId);
		return new ResponseEntity<PacienteDto>(pacienteDto, HttpStatus.OK);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PacienteDto> create(@Valid @RequestBody CreatePacienteDto pacienteDto) {
		PacienteDto pacienteCriado = pacienteService.create(pacienteDto);
		return new ResponseEntity<PacienteDto>(pacienteCriado, HttpStatus.CREATED);
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PacienteDto> update(@Valid @RequestBody UpdatePacienteDto pacienteDto) {
		PacienteDto pacienteAtualizado = pacienteService.update(pacienteDto);
		return new ResponseEntity<PacienteDto>(pacienteAtualizado, HttpStatus.OK);
	}
	
	
	@DeleteMapping(value = "/{pacienteId}")
	public ResponseEntity<Void> delete(@PathVariable Long pacienteId) {
		pacienteService.delete(pacienteId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
