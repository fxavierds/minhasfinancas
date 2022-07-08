package com.financas.minhasfinancas.model.entity;

import javax.persistence.*;

import lombok.Builder;
import lombok.Data;

@Entity
@Table( name = "usuario", schema = "financas")
@Data
@Builder
public class Usuario {
	
	@Id
	@Column (name="id") 
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (name="nome")
	private String nome;
	
	@Column (name="email")
	private String email;
	
	@Column (name="senha")
	private String senha;		
	
}

