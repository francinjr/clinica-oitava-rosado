package com.francinjr.clinicaoitavarosado.exceptions.handler;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ExceptionResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer status;
	private String title;
	private String detail;
	private List<ValidationField> fields;
}
