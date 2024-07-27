package com.francinjr.clinicaoitavarosado.exceptions;

import java.util.List;

import org.springframework.validation.FieldError;


public class ResourceAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private List<FieldError> camposInvalidos;
    private String exceptionMessage;
    
    public ResourceAlreadyExistsException(String exceptionMessage, 
    		List<FieldError> camposInvalidos) {
    	this.exceptionMessage = exceptionMessage;
    	this.camposInvalidos = camposInvalidos;
    }
    
	public List<FieldError> getCamposInvalidos() {
		return camposInvalidos;
	}
	public void setCamposInvalidos(List<FieldError> camposInvalidos) {
		this.camposInvalidos = camposInvalidos;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
    
    
}
