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

import com.francinjr.clinicaoitavarosado.dtos.agendamento.AgendamentoMedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.agendamento.CreateAgendamentoMedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.agendamento.UpdateAgendamentoMedicoDto;
import com.francinjr.clinicaoitavarosado.services.AgendamentoMedicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/agendamentos/v1")
@Tag(name = "Agendamentos Médicos", description = "Endpoint para agendamentos médicos")
public class AgendamentoMedicoController {

	@Autowired
	AgendamentoMedicoService agendamentoMedicoService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Pegar todos os agendamentos médicos", description = "Pega todos os agendamentos médicos", tags = {
	"Agendamentos Médicos" }, responses = {
			@ApiResponse(description = "Sucess", responseCode = "200", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AgendamentoMedicoDto.class)))

			}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
	})
	public ResponseEntity<PagedModel<EntityModel<AgendamentoMedicoDto>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "10") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
			) {
		
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "paciente.pessoa.nomeCompleto"));
		
		return ResponseEntity.ok(agendamentoMedicoService.findAll(pageable));
	}
	
	


	@GetMapping(value = "/{agendamentoId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Buscar um agendamento médico", description = "Busca um agendamento médico por id", tags = {
	"Agendamentos Médicos" }, responses = {
			@ApiResponse(description = "Sucess", responseCode = "200", content = @Content(schema = @Schema(implementation = AgendamentoMedicoDto.class))),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
	})
	public ResponseEntity<AgendamentoMedicoDto> findById(@PathVariable Long agendamentoId) {
		AgendamentoMedicoDto agendamentoDto = agendamentoMedicoService.findById(agendamentoId);
		return new ResponseEntity<AgendamentoMedicoDto>(agendamentoDto, HttpStatus.OK);
	}
	
	
	

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Criar um agendamento médico", description = "Cria um agendamento "
			+ "médico mandando os dados do agendamento, com o id de um médico e de um paciente", tags = {
	"Agendamentos Médicos" }, responses = {
			@ApiResponse(description = "Created", responseCode = "201", content = @Content(schema = @Schema(implementation = AgendamentoMedicoDto.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
	})
	public ResponseEntity<AgendamentoMedicoDto> create(@Valid @RequestBody CreateAgendamentoMedicoDto agendamentoDto) {
		AgendamentoMedicoDto agendamentoCriado = agendamentoMedicoService.create(agendamentoDto);
		return new ResponseEntity<AgendamentoMedicoDto>(agendamentoCriado, HttpStatus.CREATED);
	}
	
	
	

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Atualizar um agendamento médico", description = "Atualiza um agendamento "
			+ "médico mandando os dados do agendamento", tags = {
	"Agendamentos Médicos" }, responses = {
			@ApiResponse(description = "Updated", responseCode = "200", content = @Content(schema = @Schema(implementation = AgendamentoMedicoDto.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
	})
	public ResponseEntity<AgendamentoMedicoDto> update(@Valid @RequestBody UpdateAgendamentoMedicoDto agendamentoDto) {
		AgendamentoMedicoDto agendamentoAtualizado = agendamentoMedicoService.update(agendamentoDto);
		return new ResponseEntity<AgendamentoMedicoDto>(agendamentoAtualizado, HttpStatus.OK);
	}
	
	
	@DeleteMapping(value = "/{agendamentoId}")
	@Operation(summary = "Deletar um agendamento médico", description = "Deleta um agendamento "
			+ "médico mandando o id de um agendamento", tags = {
	"Agendamentos Médicos" }, responses = {
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
	})
	public ResponseEntity<Void> delete(@PathVariable Long agendamentoId) {
		agendamentoMedicoService.delete(agendamentoId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
