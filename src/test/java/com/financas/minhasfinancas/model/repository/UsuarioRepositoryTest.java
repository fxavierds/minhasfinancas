package com.financas.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.financas.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class) 
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificaExistenciaDeEmail() {
		Usuario usuario = Usuario.builder().nome("Usuario").email("usuario@gmail.com").build();
		entityManager.persist(usuario);
				
		boolean result = repository.existsByEmail("usuario@gmail.com");
		
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalseQuandoNaoHouverUsuarioComEmail() {
				
		boolean result = repository.existsByEmail("usuario@gmail.com");
		
		Assertions.assertThat(result).isFalse();		
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		Usuario usuario = Usuario.builder().nome("Usuario").email("usuario@gmail.com").build();
		
		Usuario usuarioSalvo = repository.save(usuario);
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		Optional<Usuario> result = repository.findByEmail("usuario@gmail.com");
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioQuandoBuscarUmUsuarioPorEmailQueNaoExisteNaBase() {
				
		Optional<Usuario> result = repository.findByEmail("usuario@gmail.com");
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	public static Usuario criarUsuario() {
		Usuario usuario = Usuario.builder().nome("Usuario").email("usuario@gmail.com").build();
		return usuario;
	}
}
