package com.financas.minhasfinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import com.financas.minhasfinancas.exceptions.ErroAutenticacao;
import com.financas.minhasfinancas.exceptions.RegraNegocioException;
import com.financas.minhasfinancas.model.entity.Usuario;
import com.financas.minhasfinancas.model.repository.UsuarioRepository;
import com.financas.minhasfinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;	
	
	@MockBean
	UsuarioRepository repository;
	
	@Test
	public void deveSalvarUmUsuario() {
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1L)
				.email("email@email.com")
				.nome("nome")
				.senha("senha")
				.build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		assertNotNull(usuarioSalvo);	
		assertThat(usuarioSalvo.getId()).isEqualTo(1L);	
		assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");		
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
				
		service.salvarUsuario(usuario);
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(11l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		Usuario result = service.autenticar(email, senha);
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarErroCadastradoComEmailInformado() {
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
				
		Throwable exception =  Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado.");
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).id(11l).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		Throwable exception =  Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
	}
	
	@Test
	public void deveValidarEmail() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		service.validarEmail("email@email.com");				
	}
	
	@Test
	@DisplayName("Verifica se já existe um email")
	public void deveLancarErroAoValidarEmailQuandoNaoHouverEmailCadastrado() {
		Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		repository.save(usuario);
		
		assertThrows(RegraNegocioException.class, () -> {
			service.validarEmail("email@email.com");
		}, "RegraNegocioException was expected");
		
	}
}
