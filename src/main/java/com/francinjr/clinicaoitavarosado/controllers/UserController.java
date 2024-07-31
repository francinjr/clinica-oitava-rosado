package com.francinjr.clinicaoitavarosado.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.francinjr.clinicaoitavarosado.dtos.usuario.CreateUserDto;
import com.francinjr.clinicaoitavarosado.dtos.usuario.RequestEmailRecoverAccessDto;
import com.francinjr.clinicaoitavarosado.dtos.usuario.ResetPasswordRequestDto;
import com.francinjr.clinicaoitavarosado.dtos.usuario.UserDto;
import com.francinjr.clinicaoitavarosado.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios/v1")
public class UserController {

	@Autowired
	UsuarioService userService;

	/*@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PacienteDto> findAll() {
		
		
		return ResponseEntity.ok(userService.findAll());
	}


	@GetMapping(value = "/{pacienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PacienteDto> findById(@PathVariable Long pacienteId) {
		PacienteDto pacienteDto = userService.findById(pacienteId);
		return new ResponseEntity<PacienteDto>(pacienteDto, HttpStatus.OK);
	}*/

	
	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUserDto usuarioDto) throws Exception {
		UserDto usuarioCriado = userService.create(usuarioDto);
		return new ResponseEntity<UserDto>(usuarioCriado, HttpStatus.CREATED);
	}
	
	
			
	@PostMapping(value = "/recoverAccess", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> recoverAccess(@RequestBody RequestEmailRecoverAccessDto requestEmailRecoverAccessDto) {
		userService.recoverAccess(requestEmailRecoverAccessDto.getEmail());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequest) throws Exception {
		userService.resetPassword(resetPasswordRequest);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	

	/*@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PacienteDto> update(@Valid @RequestBody UpdatePacienteDto pacienteDto) {
		PacienteDto pacienteAtualizado = userService.update(pacienteDto);
		return new ResponseEntity<PacienteDto>(pacienteAtualizado, HttpStatus.OK);
	}
	
	
	@DeleteMapping(value = "/{pacienteId}")
	public ResponseEntity<Void> delete(@PathVariable Long pacienteId) {
		userService.delete(pacienteId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}*/
}
