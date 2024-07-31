package com.francinjr.clinicaoitavarosado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.francinjr.clinicaoitavarosado.entities.Usuario;



@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	//@Query("SELECT u FROM Usuario u WHERE u.userName =:userName")
	//Usuario findByUsername(@Param("userName") String userName);
	Usuario findByUserName(String username);
	
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_permission (id_user, id_permission) VALUES (:userId, :permissionId)", nativeQuery = true)
    void addPermissionToUser(@Param("userId") Long userId, @Param("permissionId") Long permissionId);
    
    
    Usuario findByEmail(String email);
    /*@Modifying
    @Transactional
    @Query("INSERT INTO UserPermission (idUser, idPermission) VALUES (:idUser, :idPermission)")
    void addPermissionToUser(Long idUser, Long idPermission);*/
}
