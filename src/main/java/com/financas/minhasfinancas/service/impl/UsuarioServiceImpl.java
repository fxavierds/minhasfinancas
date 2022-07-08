package com.financas.minhasfinancas.service.impl;

import com.financas.minhasfinancas.model.entity.Usuario;
import com.financas.minhasfinancas.model.repository.UsuarioRepository;
import com.financas.minhasfinancas.service.UsuarioService;

public class UsuarioServiceImpl implements UsuarioService{
	private UsuarioRepository usuarioRepository;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validarEmail(String email) {
		// TODO Auto-generated method stub
		
	}

}
