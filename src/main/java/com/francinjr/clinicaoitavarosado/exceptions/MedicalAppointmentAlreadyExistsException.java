package com.francinjr.clinicaoitavarosado.exceptions;

public class MedicalAppointmentAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MedicalAppointmentAlreadyExistsException(String message) {
		super(message);
	}
}
