package com.francinjr.clinicaoitavarosado.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.francinjr.clinicaoitavarosado.entities.AgendamentoMedico;

public interface AgendamentoMedicoRepository extends JpaRepository<AgendamentoMedico, Long> {
	@Query("SELECT COUNT(a) = 0 " + "FROM agendamentos a " + "WHERE a.medico.id = :medicoId "
			+ "AND a.dataConsulta = :dataConsulta " + "AND (a.inicio < :fim AND a.fim > :inicio)")
	boolean isHorarioDisponivel(@Param("medicoId") Long medicoId, @Param("dataConsulta") LocalDate dataConsulta,
			@Param("inicio") LocalTime inicio, @Param("fim") LocalTime fim);
	/*
	 * Optional<AgendamentoMedico> findByMedicoIdAndDataConsultaAndInicioAndFim(Long
	 * medicoId, LocalDate dataConsulta, LocalTime inicio, LocalTime fim);
	 */

	Optional<AgendamentoMedico> findByMedicoIdAndDataConsultaAndInicioAndFim(@Param("medicoId") Long medicoId,

			@Param("dataConsulta") LocalDate dataConsulta,

			@Param("inicio") LocalTime inicio,

			@Param("fim") LocalTime fim);

}
