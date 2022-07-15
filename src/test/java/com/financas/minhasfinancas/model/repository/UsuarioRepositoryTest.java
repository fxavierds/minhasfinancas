package com.financas.minhasfinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.financas.minhasfinancas.model.entity.Usuario;

@SpringBootTest
@ExtendWith(SpringExtension.class) 
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveVerificaExistenciaDeEmail() {
		Usuario usuario = Usuario.builder().nome("Usuario").email("usuario@gmail.com").build();
		repository.save(usuario);
		
		boolean result = repository.existsByEmail("usuario@gmail.com");
		
		Assertions.assertThat(result).isTrue();
	}
}
