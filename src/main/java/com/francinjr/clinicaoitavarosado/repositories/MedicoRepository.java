package com.francinjr.clinicaoitavarosado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.francinjr.clinicaoitavarosado.entities.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
	//Medico findByCpf(String cpf);
	//Medico findByCpfOrTelefoneOrEmail(String cpf, String telefone, String email);
}
