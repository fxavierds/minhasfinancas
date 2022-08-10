package com.financas.minhasfinancas.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.NotThrownAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.financas.minhasfinancas.exceptions.RegraNegocioException;
import com.financas.minhasfinancas.model.entity.Usuario;
import com.financas.minhasfinancas.model.repository.UsuarioRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioService service;	
	UsuarioRepository repository;
	
	@BeforeAll
	public void setup() {
		
	}
	
	@Test
	public void deveValidarEmail() {
		UsuarioRepository usuarioRepositoryMock = Mockito.mock(UsuarioRepository.class);
		repository.deleteAll();
		
		assertThrows(NullPointerException.class, () -> {
			service.validarEmail("email@email.com");
		}, "NumberFormatException was expected");
		
		
	}
	
	@Test
	@DisplayName("Verifica se já existe um email")
	public void deveLancarErroAoValidarEmailQuandoNaoHouverEmailCadastrado() {
		Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		repository.save(usuario);
		
		assertThrows(RegraNegocioException.class, () -> {
			service.validarEmail("email@email.com");
		}, "NumberFormatException was expected");
		
	}
}
