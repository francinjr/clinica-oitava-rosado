package com.francinjr.clinicaoitavarosado.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.hateoas.EntityModel;

import com.francinjr.clinicaoitavarosado.dtos.paciente.PacienteDto;

public interface PacienteService {
	PagedModel<EntityModel<PacienteDto>> findAll(Pageable pageable);
    PacienteDto findById(Long id);
    PacienteDto create(PacienteDto paciente);
    PacienteDto update(PacienteDto paciente);
    void delete(Long id);
}
