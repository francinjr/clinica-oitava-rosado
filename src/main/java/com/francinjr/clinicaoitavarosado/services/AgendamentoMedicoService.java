package com.francinjr.clinicaoitavarosado.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.francinjr.clinicaoitavarosado.controllers.AgendamentoMedicoController;
import com.francinjr.clinicaoitavarosado.dtos.agendamento.AgendamentoMedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.agendamento.CreateAgendamentoMedicoDto;
import com.francinjr.clinicaoitavarosado.dtos.agendamento.UpdateAgendamentoMedicoDto;
import com.francinjr.clinicaoitavarosado.entities.AgendamentoMedico;
import com.francinjr.clinicaoitavarosado.entities.Medico;
import com.francinjr.clinicaoitavarosado.entities.Paciente;
import com.francinjr.clinicaoitavarosado.exceptions.MedicalAppointmentAlreadyExistsException;
import com.francinjr.clinicaoitavarosado.exceptions.ResourceNotFoundException;
import com.francinjr.clinicaoitavarosado.exceptions.UniqueConstraintViolationException;
import com.francinjr.clinicaoitavarosado.mappers.Mapper;
import com.francinjr.clinicaoitavarosado.repositories.AgendamentoMedicoRepository;
import com.francinjr.clinicaoitavarosado.repositories.MedicoRepository;
import com.francinjr.clinicaoitavarosado.repositories.PacienteRepository;

@Service
public class AgendamentoMedicoService {

	@Autowired
	private AgendamentoMedicoRepository agendamentoMedicoRepository;
	
	@Autowired
	PacienteRepository pacienteRepository;
	
	@Autowired
	MedicoRepository medicoRepository;
	
	@Autowired
	PagedResourcesAssembler<AgendamentoMedicoDto> assembler;

	public PagedModel<EntityModel<AgendamentoMedicoDto>> findAll(Pageable pageable) {
		
		var agendamentoPage = agendamentoMedicoRepository.findAll(pageable);
		
		var agendamentoDtosPage = agendamentoPage.map(ag -> Mapper.parseObject(ag, AgendamentoMedicoDto.class));
		
		agendamentoDtosPage.map(
				ag -> ag.add(
						linkTo(methodOn(AgendamentoMedicoController.class)
								.findById(ag.getKey())).withSelfRel()));
		
		Link link = linkTo(
				methodOn(AgendamentoMedicoController.class)
					.findAll(pageable.getPageNumber(),
							pageable.getPageSize(),
							"asc")).withSelfRel();
		return assembler.toModel(agendamentoDtosPage, link);
	}


	public AgendamentoMedicoDto findById(Long agendamentoId) {
		AgendamentoMedico entity = agendamentoMedicoRepository.findById(agendamentoId)
				.orElseThrow(() -> new ResourceNotFoundException("Agendamento com id " 
						+ agendamentoId + " não foi encontrado"));

		AgendamentoMedicoDto dto = Mapper.parseObject(entity, AgendamentoMedicoDto.class);

		dto.add(linkTo(methodOn(AgendamentoMedicoController.class).findById(agendamentoId)).withSelfRel());
		return dto;
	}

	
	@Transactional
	public AgendamentoMedicoDto create(CreateAgendamentoMedicoDto agendamento) throws UniqueConstraintViolationException {
		AgendamentoMedico entity = Mapper.parseObject(agendamento, AgendamentoMedico.class);
		
		boolean dataDisponivel = existsAgendamentoDataHorario(entity);
		
		if(!dataDisponivel) {
			throw new MedicalAppointmentAlreadyExistsException("Já existe um agendamento com "
				+ "o médico " + agendamento.getMedico().getPessoa().getNomeCompleto() 
				+ ", na data " + agendamento.getDataConsulta() + " das " 
				+ agendamento.getInicio() + " até " + agendamento.getFim());
		}
			
		Medico medicoEncontrado = medicoRepository.findById(agendamento.getMedico().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Médico com id " 
						+ agendamento.getMedico().getId() + " não foi encontrado"));
			
		Paciente pacienteEncontrado = pacienteRepository.findById(agendamento.getPaciente().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Paciente com id " 
							+ agendamento.getPaciente().getId() + " não foi encontrado"));
		
		AgendamentoMedico agendamentoSalvo = agendamentoMedicoRepository.save(entity);
				
		agendamentoSalvo.setMedico(medicoEncontrado);
		agendamentoSalvo.setPaciente(pacienteEncontrado);
			
			
		AgendamentoMedicoDto dto = Mapper.parseObject(agendamentoSalvo, AgendamentoMedicoDto.class);
		dto.getMedico().getPessoa().setNomeCompleto(medicoEncontrado.getPessoa().getNomeCompleto());
		dto.getPaciente().getPessoa().setNomeCompleto(pacienteEncontrado.getPessoa().getNomeCompleto());
		dto.add(linkTo(methodOn(AgendamentoMedicoController.class).findById(dto.getKey())).withSelfRel());
		return dto;
	}

	
	@Transactional
	public AgendamentoMedicoDto update(UpdateAgendamentoMedicoDto agendamento) throws UniqueConstraintViolationException {
		
		AgendamentoMedico agendamentoEncontrado = agendamentoMedicoRepository
				.findById(agendamento.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Agendamento com id " 
						+ agendamento.getId() + " não foi encontrado"));
		
		AgendamentoMedico entity = Mapper.parseObject(agendamento, AgendamentoMedico.class);
		
		boolean dataDisponivel = existsAgendamentoDataHorario(entity);
		
		if(!dataDisponivel) {
			throw new MedicalAppointmentAlreadyExistsException("Já existe um agendamento com "
				+ "o médico " + agendamento.getMedico().getPessoa().getNomeCompleto() 
				+ ", na data " + agendamento.getDataConsulta() + " das " 
				+ agendamento.getInicio() + " até " + agendamento.getFim());
		}
			
		
		Medico medicoEncontrado = medicoRepository.findById(agendamento.getMedico().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Médico com id " 
						+ agendamento.getMedico().getId() + " não foi encontrado"));
			
		Paciente pacienteEncontrado = pacienteRepository.findById(agendamento.getPaciente().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Paciente com id " 
							+ agendamento.getPaciente().getId() + " não foi encontrado"));
		
		AgendamentoMedico agendamentoSalvo = agendamentoMedicoRepository.save(entity);
			
			
		agendamentoSalvo.setMedico(medicoEncontrado);
		agendamentoSalvo.setPaciente(pacienteEncontrado);
			
			
		AgendamentoMedicoDto dto = Mapper.parseObject(agendamentoSalvo, AgendamentoMedicoDto.class);
		dto.getMedico().getPessoa().setNomeCompleto(medicoEncontrado.getPessoa().getNomeCompleto());
		dto.getPaciente().getPessoa().setNomeCompleto(pacienteEncontrado.getPessoa().getNomeCompleto());
		dto.add(linkTo(methodOn(AgendamentoMedicoController.class).findById(dto.getKey())).withSelfRel());
		return dto;
	}
	
	
	@Transactional
	public void delete(Long agendamentoId) {
		AgendamentoMedico entity = agendamentoMedicoRepository.findById(agendamentoId)
				.orElseThrow(() -> new ResourceNotFoundException("Não foi possível deletar, "
						+ "não existe um agendamento com id " + agendamentoId));

		agendamentoMedicoRepository.delete(entity);
	}
	
	
	
	private boolean existsAgendamentoDataHorario(AgendamentoMedico agendamento) {
		/*boolean dataDisponivel = agendamentoMedicoRepository.isHorarioDisponivel(
				agendamento.getMedico().getId(), 
				agendamento.getDataConsulta(), agendamento.getInicio(), 
				agendamento.getFim());
		
		if(dataDisponivel) {
			return true;
		}
		return false;*/
		Optional<AgendamentoMedico> ag = agendamentoMedicoRepository
				.findByMedicoIdAndDataConsultaAndInicioAndFim(agendamento.getMedico().getId(), 
						agendamento.getDataConsulta(), agendamento.getInicio(), 
						agendamento.getFim());
		
		if(ag.isPresent()) {
			return false;
		}
		return true;
	}
}
