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

import com.francinjr.clinicaoitavarosado.dtos.medico.CreateMedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.medico.MedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.medico.UpdateMedicoDto;
import com.francinjr.clinicaoitavarosado.services.MedicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medicos/v1")
public class MedicoController {

	@Autowired
	MedicoService medicoService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedModel<EntityModel<MedicoDto>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "10") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
			) {
		
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nomeCompleto"));
		
		return ResponseEntity.ok(medicoService.findAll(pageable));
	}


	@GetMapping(value = "/{medicoId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MedicoDto> findById(@PathVariable Long medicoId) {
		MedicoDto medicoDto = medicoService.findById(medicoId);
		return new ResponseEntity<MedicoDto>(medicoDto, HttpStatus.OK);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MedicoDto> create(@Valid @RequestBody CreateMedicoDto medicoDto) {
		MedicoDto medicoCriado = medicoService.create(medicoDto);
		return new ResponseEntity<MedicoDto>(medicoCriado, HttpStatus.CREATED);
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MedicoDto> update(@Valid @RequestBody UpdateMedicoDto medicoDto) {
		MedicoDto medicoAtualizado = medicoService.update(medicoDto);
		return new ResponseEntity<MedicoDto>(medicoAtualizado, HttpStatus.OK);
	}
	
	
	@DeleteMapping(value = "/{medicoId}")
	public ResponseEntity<Void> delete(@PathVariable Long medicoId) {
		medicoService.delete(medicoId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
