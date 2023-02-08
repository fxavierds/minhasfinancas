package com.financas.minhasfinancas.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.financas.minhasfinancas.api.dto.UsuarioDTO;
import com.financas.minhasfinancas.api.resource.UsuarioResource;
import com.financas.minhasfinancas.model.entity.Usuario;
import com.financas.minhasfinancas.service.UsuarioService;

@ExtendWith(SpringExtension.class) 
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {
	static final String api = "usuario/api";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService service;
	
	@Test
	public void deveAutenticaUmUsuario() {
		String email = "teste@teste.com.br";
		String senha = "123";
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		
		Usuario usuario = Usuario.builder().id(1L).email(email).senha(senha).build();
	}

}
