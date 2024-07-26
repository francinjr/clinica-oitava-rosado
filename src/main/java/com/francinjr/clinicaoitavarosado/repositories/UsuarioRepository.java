package com.francinjr.clinicaoitavarosado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.francinjr.clinicaoitavarosado.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
