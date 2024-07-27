package com.francinjr.clinicaoitavarosado.exceptions.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.francinjr.clinicaoitavarosado.exceptions.ResourceAlreadyExistsException;
import com.francinjr.clinicaoitavarosado.exceptions.ResourceNotFoundException;

@ControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(
			Exception ex, WebRequest request) {
		
		ExceptionResponse exceptionResponse = new ExceptionResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				null,
				"Erro",
				ex.getMessage(),
				null);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	private ResponseEntity<ExceptionResponse> handleResourceNotFound(ResourceNotFoundException exception) {
		ExceptionResponse threatResponse = new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
				null, "Recurso não encontrado", exception.getMessage(), null);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
	}

	//@ExceptionHandler(MethodArgumentNotValidException.class)
	// Para o Bean Validation
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		List<ValidationField> validationFields = new ArrayList<ValidationField>();

		for (FieldError error : exception.getBindingResult().getFieldErrors()) {
			validationFields.add(new ValidationField(error.getField(), error.getDefaultMessage()));
		}

		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), null,
				"Campos inválidos", "Há campos que não foram preenchidos corretamente", validationFields);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
	}
	
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	private ResponseEntity<Object> handleMethodArgumentNotValid(ResourceAlreadyExistsException exception) {

		List<ValidationField> validationFields = new ArrayList<ValidationField>();

		for (FieldError error : exception.getCamposInvalidos()) {
			validationFields.add(new ValidationField(error.getField(), error.getDefaultMessage()));
		}

		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.CONFLICT.value(), null,
				"Campos inválidos", exception.getExceptionMessage(), validationFields);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
	}
}
