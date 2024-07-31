package com.francinjr.clinicaoitavarosado.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;

import com.francinjr.clinicaoitavarosado.dtos.paciente.CreatePacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.PacienteDto;
import com.francinjr.clinicaoitavarosado.dtos.paciente.UpdatePacienteDto;
import com.francinjr.clinicaoitavarosado.entities.Paciente;
import com.francinjr.clinicaoitavarosado.exceptions.UniqueConstraintViolationException;
import com.francinjr.clinicaoitavarosado.mappers.Mapper;
import com.francinjr.clinicaoitavarosado.repositories.PacienteRepository;

public class PacienteServiceMockConfiguration {

    @Mock
    protected PacienteRepository pacienteRepository;

    @Mock
    protected PessoaService pessoaService;

    @Mock
    protected PagedResourcesAssembler<PacienteDto> assembler;

    @InjectMocks
    protected PacienteService pacienteService;

    protected void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    protected void mockFindAll(Pageable pageable, Paciente paciente) {
        Page<Paciente> pacientePage = new PageImpl<>(List.of(paciente));
        when(pacienteRepository.findAll(pageable)).thenReturn(pacientePage);
        PacienteDto pacienteDto = new PacienteDto();
        when(Mapper.parseObject(paciente, PacienteDto.class)).thenReturn(pacienteDto);
    }

    protected void mockFindById(Long pacienteId, Paciente paciente) {
        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));
        PacienteDto pacienteDto = new PacienteDto();
        when(Mapper.parseObject(paciente, PacienteDto.class)).thenReturn(pacienteDto);
    }

    protected void mockCreate(CreatePacienteDto createDto, Paciente paciente, Paciente savedPaciente, PacienteDto pacienteDto) throws UniqueConstraintViolationException {
        when(Mapper.parseObject(createDto, Paciente.class)).thenReturn(paciente);
        when(pacienteRepository.save(paciente)).thenReturn(savedPaciente);
        when(Mapper.parseObject(savedPaciente, PacienteDto.class)).thenReturn(pacienteDto);
        when(pessoaService.validatePersonUniqueFields(any())).thenReturn(new ArrayList<>());
    }

    protected void mockUpdate(UpdatePacienteDto updateDto, Paciente existingPaciente, Paciente updatedPaciente, PacienteDto pacienteDto) throws UniqueConstraintViolationException {
        when(pacienteRepository.findById(anyLong())).thenReturn(Optional.of(existingPaciente));
        when(Mapper.parseObject(updateDto, Paciente.class)).thenReturn(updatedPaciente);
        when(pacienteRepository.save(updatedPaciente)).thenReturn(updatedPaciente);
        when(Mapper.parseObject(updatedPaciente, PacienteDto.class)).thenReturn(pacienteDto);
        when(pessoaService.validatePersonUniqueFields(any())).thenReturn(new ArrayList<>());
    }

    protected void mockDelete(Long pacienteId, Paciente paciente) {
        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));
    }
}
