package com.francinjr.clinicaoitavarosado.repositories;

import com.francinjr.clinicaoitavarosado.entities.AgendamentoMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoMedicoRepository extends JpaRepository<AgendamentoMedico, Long> {
}
