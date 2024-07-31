package com.francinjr.clinicaoitavarosado.services;

import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.francinjr.clinicaoitavarosado.dtos.usuario.CreateUserDto;
import com.francinjr.clinicaoitavarosado.dtos.usuario.ResetPasswordRequestDto;
import com.francinjr.clinicaoitavarosado.dtos.usuario.UserDto;
import com.francinjr.clinicaoitavarosado.entities.Usuario;
import com.francinjr.clinicaoitavarosado.exceptions.ResourceNotFoundException;
import com.francinjr.clinicaoitavarosado.mappers.Mapper;
import com.francinjr.clinicaoitavarosado.repositories.UsuarioRepository;


@Service
public class UsuarioService implements UserDetailsService {
	
	private Logger logger = Logger.getLogger(UsuarioService.class.getName());
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	EmailService emailService;
	
	public UsuarioService(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Finding one user by name " + username + "!");
		var user = repository.findByUserName(username);
		if (user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
	}
	

	@Transactional
	public UserDto create(CreateUserDto usuario) throws Exception {
		System.out.println("O usuário na criação é: " + usuario.getUserName());
		System.out.println("A senha na criação é: " + usuario.getPassword());
		var user = repository.findByUserName(usuario.getUserName());
		if (user != null) {
			throw new Exception("Já existe o nome de usuário: " + usuario.getUserName());
		}
		
		Usuario entity = new Usuario();
		entity.setUserName(usuario.getUserName());
		
		String encodedPassword = passwordEncoder.encode(usuario.getPassword());
		entity.setPassword(encodedPassword);
		
		entity.setEmail(usuario.getEmail());
		
		entity.setAccountNonExpired(true);
		entity.setAccountNonLocked(true);
		entity.setCredentialsNonExpired(true);
		entity.setEnabled(true);
		

		Usuario usuarioCriado = repository.save(entity);
		
		addPermissionToUser(usuarioCriado.getId(), 1L);
		
		UserDto usuarioCriadoDto = Mapper.parseObject(usuarioCriado, UserDto.class);
		return usuarioCriadoDto;
	}
	
	
	public void recoverAccess(String email) {//1
		Usuario usuarioEncontrado = repository.findByEmail(email);
		
		if(usuarioEncontrado == null) {
			throw new ResourceNotFoundException("Não existe um usuário cadastrado o email "
					+ "fornecido");
		}
		
		sendVerificationCode(email);//2
	}
	
	
	public void resetPassword(ResetPasswordRequestDto resetPasswordRequest) throws Exception {
		Usuario usuarioEncontrado = repository.findByEmail(resetPasswordRequest.getEmail());
		
		if(usuarioEncontrado == null) {
			throw new ResourceNotFoundException("Não foi possível alterar senha, não existe "
					+ "um usuário com o email fornecido");
		}
		
		
		if(!resetPasswordRequest.getPassword()
				.equals(resetPasswordRequest.getPasswordConfirmation())) {
			throw new Exception("A senha e confirmação de senha precisam ser iguais");
		}
		
		if(resetPasswordRequest.getPassword().equals(usuarioEncontrado.getPassword())) {
			throw new Exception("A nova senha precisa ser diferente da senha atual");
		}
		
		if(!resetPasswordRequest.getVerificationCode()
				.equals(usuarioEncontrado.getCurrentVerificationCode())) {
			throw new Exception("Código de verificação inválido");
		}
		
		String encodedPassword = passwordEncoder.encode(resetPasswordRequest.getPassword());
		usuarioEncontrado.setPassword(encodedPassword);
		
		repository.save(usuarioEncontrado);
	}
	
	
	private void sendVerificationCode(String email) {
		String verificationCode = createVerificationCode();//3
		
		saveVerificationCodeToUser(email, verificationCode);//4
		
		emailService.sendEmail(email, "Código para recuperação de Usuário", "Esse é seu "
				+ "código de recuperação " + verificationCode);//5
	}
	
	
	private String createVerificationCode() {
		return UUID.randomUUID().toString();
	}

	
	private void saveVerificationCodeToUser(String email, String verificationCode) {
		Usuario usuarioEncontrado = repository.findByEmail(email);
		
		usuarioEncontrado.setCurrentVerificationCode(verificationCode);
		repository.save(usuarioEncontrado);
	}
	
	
    private void addPermissionToUser(Long userId, Long permissionId) {
    	repository.addPermissionToUser(userId, permissionId);
    }
}
