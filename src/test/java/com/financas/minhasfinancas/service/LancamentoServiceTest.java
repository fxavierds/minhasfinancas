package com.financas.minhasfinancas.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.financas.minhasfinancas.exceptions.RegraNegocioException;
import com.financas.minhasfinancas.model.entity.Lancamento;
import com.financas.minhasfinancas.model.enums.StatusLancamento;
import com.financas.minhasfinancas.model.repository.LancamentoRepository;
import com.financas.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.financas.minhasfinancas.service.impl.LancamentoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validarLancamento(lancamentoSalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoSalvar)).thenReturn(lancamentoSalvo);
		
		Lancamento lancamento = service.salvar(lancamentoSalvar);
		
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
		
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarLancamento(lancamentoSalvar);
		
		Assertions.catchThrowableOfType(() -> service.salvar(lancamentoSalvar), RegraNegocioException.class);
		
		Mockito.verify(repository, Mockito.never()).save(lancamentoSalvar);
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.doNothing().when(service).validarLancamento(lancamentoSalvo);
		
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		service.atualizar(lancamentoSalvo);
		
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarLancamentoSemId() {
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
	
		Assertions.catchThrowableOfType(() -> service.atualizar(lancamentoSalvar), NullPointerException.class);
		
		Mockito.verify(repository, Mockito.never()).save(lancamentoSalvar);
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1L);
		
		service.deletar(lancamento);
		
		Mockito.verify(repository).delete(lancamento);
	}
	
	@Test
	public void naoDeveDeletarUmLancamentoSemId() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
				
		Assertions.catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);
			
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
	}

}
