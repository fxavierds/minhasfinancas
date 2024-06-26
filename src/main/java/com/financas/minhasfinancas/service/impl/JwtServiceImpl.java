package com.financas.minhasfinancas.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.financas.minhasfinancas.model.entity.Usuario;
import com.financas.minhasfinancas.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl implements JwtService {
	
	@Value("${jwt.expiracao}")
	private String expiracao;
	
	@Value("${jwt.chave-assinatura}")
	private String chaveAssinatura;

	@Override
	public String gerarToken(Usuario usuario) {
		long exp = Long.valueOf(expiracao);
		LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);
		Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
		java.util.Date data = Date.from(instant);
		
		String token = Jwts.builder()
				.setExpiration(data)
				.setSubject(usuario.getEmail())
				.signWith(SignatureAlgorithm.HS512, chaveAssinatura)
				.compact();
		return token;
	}

	@Override
	public Claims obterClaims(String token) throws ExpiredJwtException {
		return Jwts.parser().setSigningKey(chaveAssinatura).parseClaimsJws(token).getBody();
	}

	@Override
	public boolean isTokenValid(String token) {
		try {
			Claims claims = obterClaims(token);
			java.util.Date dataExp = claims.getExpiration();
			LocalDateTime dataExpiracao = dataExp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			boolean dataHoraAtualisAfterDataExpiracao = LocalDateTime.now().isAfter(dataExpiracao);
			return !dataHoraAtualisAfterDataExpiracao;
		}catch (Exception e) {
			return false;
		}
		
	}

	@Override
	public String obterLoginUsuario(String token) {
		Claims claims = obterClaims(token);
		return claims.getSubject();
	}

}
