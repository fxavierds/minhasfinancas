package com.financas.minhasfinancas.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.financas.minhasfinancas.model.entity.Usuario;
import com.financas.minhasfinancas.model.repository.UsuarioRepository;

@Service
public class SecurityUserDetailService implements UserDetailsService {
	private UsuarioRepository usuarioRepository;
	
	
	public SecurityUserDetailService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuarioEncontrado = usuarioRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
		
		User user = (User) User.builder()
				.username(usuarioEncontrado.getEmail())
				.password(usuarioEncontrado.getSenha())
				.roles("USER").build();
		return user;
	}

}
