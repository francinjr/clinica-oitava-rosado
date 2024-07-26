package com.francinjr.clinicaoitavarosado.repositories;

import com.francinjr.clinicaoitavarosado.entities.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
}
