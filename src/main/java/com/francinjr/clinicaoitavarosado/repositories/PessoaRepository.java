package com.francinjr.clinicaoitavarosado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.francinjr.clinicaoitavarosado.entities.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	//Pessoa findByCpfOrTelefoneOrEmail(String cpf, String telefone, String email);
	boolean existsByCpf(String cpf);
	boolean existsByTelefone(String telefone);
	boolean existsByEmail(String email);
}
