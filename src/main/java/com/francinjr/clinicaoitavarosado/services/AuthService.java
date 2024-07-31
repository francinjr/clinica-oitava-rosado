package com.francinjr.clinicaoitavarosado.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.francinjr.clinicaoitavarosado.dtos.security.AccountCredentialsDto;
import com.francinjr.clinicaoitavarosado.dtos.security.TokenDto;
import com.francinjr.clinicaoitavarosado.repositories.UsuarioRepository;
import com.francinjr.clinicaoitavarosado.security.jwt.JwtTokenProvider;



@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UsuarioRepository repository;
	
	@SuppressWarnings("rawtypes")
	public ResponseEntity signin(AccountCredentialsDto data) {
		try {
			var username = data.getUsername();
			var password = data.getPassword();
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, password));
			
			var user = repository.findByUserName(username);
			
			var tokenResponse = new TokenDto();
			if (user != null) {
				tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Nome de usuário " + username 
						+ " não encontrado");
			}
			return ResponseEntity.ok(tokenResponse);
		} catch (Exception e) {
			throw new BadCredentialsException("Nome de usuário ou senha inválidos");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public ResponseEntity refreshToken(String username, String refreshToken) {
		var user = repository.findByUserName(username);
		
		var tokenResponse = new TokenDto();
		if (user != null) {
			tokenResponse = tokenProvider.refreshToken(refreshToken);
		} else {
			throw new UsernameNotFoundException("Nome de usuário " + username 
					+ " não encontrado");
		}
		return ResponseEntity.ok(tokenResponse);
	}
}
