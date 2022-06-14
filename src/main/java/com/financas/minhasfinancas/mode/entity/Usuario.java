package com.financas.minhasfinancas.mode.entity;

import javax.persistence.*;

@Entity
@Table( name = "usuario", schema = "financas")
public class Usuario {
	private Long id;
	private String nome;
	private String email;
	private String senha;
}
