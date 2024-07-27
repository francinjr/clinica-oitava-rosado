package com.francinjr.clinicaoitavarosado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.francinjr.clinicaoitavarosado.entities.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	Paciente findByCpf(String cpf);
	
	Paciente findByCpfOrRgOrTelefoneOrEmail(String cpf, String rg, String telefone, String email);
}
