package com.financas.minhasfinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.financas.minhasfinancas.exceptions.RegraNegocioException;
import com.financas.minhasfinancas.model.entity.Lancamento;
import com.financas.minhasfinancas.model.entity.Usuario;
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

	@SuppressWarnings("unchecked")
	@Test
	public void deveFiltrarLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1L);

		List<Lancamento> lista = new ArrayList<Lancamento>();
		lista.add(lancamento);

		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

		List<Lancamento> resultado = service.buscar(lancamento);

		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
	}

	@Test
	public void deveAtualizarStatusDeUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1L);
		lancamento.setStatus(StatusLancamento.PENDENTE);

		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		doReturn(lancamento).when(service).atualizar(lancamento);

		service.atualizarStatus(lancamento, novoStatus);
		Assertions.assertThat(lancamento.getStatus() == novoStatus);
		Mockito.verify(service).atualizar(lancamento);
	}

	@Test
	public void deveObterUmLancamentoPorId() {
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);

		when(repository.findById(id)).thenReturn(Optional.of(lancamento));

		Optional<Lancamento> resultado = service.obterPorId(id);

		assertThat(resultado.isPresent()).isTrue();
	}

	@Test
	public void deveRetornarVazioQuandoLancamentoNaoExistir() {
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);

		when(repository.findById(id)).thenReturn(Optional.empty());

		Optional<Lancamento> resultado = service.obterPorId(id);

		assertThat(resultado.isPresent()).isFalse();
	}

	@Test
	public void deveLancarExececaoDescricaoInvalida() {
		
		Lancamento lancamento = new Lancamento();
		RegraNegocioException thrown = assertThrows(RegraNegocioException.class,
				() -> service.validarLancamento(lancamento), "Informe uma descrição válida");

		assertTrue(thrown.getMessage().contentEquals("Informe uma descrição válida"));
		
		lancamento.setDescricao("");
		thrown = assertThrows(RegraNegocioException.class,
				() -> service.validarLancamento(lancamento), "Informe uma descrição válida");

		assertTrue(thrown.getMessage().contentEquals("Informe uma descrição válida"));
		
		lancamento.setDescricao("salario");
		thrown = assertThrows(RegraNegocioException.class,
				() -> service.validarLancamento(lancamento), "Informe um mês válido");

		assertTrue(thrown.getMessage().contentEquals("Informe um mês válido"));
		
		lancamento.setMes(1);
		thrown = assertThrows(RegraNegocioException.class,
				() -> service.validarLancamento(lancamento), "Informe um ano válido\"");

		assertTrue(thrown.getMessage().contentEquals("Informe um ano válido"));
		
		lancamento.setAno(2021);
		thrown = assertThrows(RegraNegocioException.class,
				() -> service.validarLancamento(lancamento), "Informe um usuário válido");

		assertTrue(thrown.getMessage().contentEquals("Informe um usuário válido"));
		Usuario us = new Usuario();
		us.setId(1L);
		lancamento.setUsuario(us);
		thrown = assertThrows(RegraNegocioException.class,
				() -> service.validarLancamento(lancamento), "Informe um valor válido\"");

		assertTrue(thrown.getMessage().contentEquals("Informe um valor válido"));
		
		lancamento.setValor(BigDecimal.ONE);
		thrown = assertThrows(RegraNegocioException.class,
				() -> service.validarLancamento(lancamento), "Informe um tipo válido\"");

		assertTrue(thrown.getMessage().contentEquals("Informe um tipo válido"));

		
	}

}
