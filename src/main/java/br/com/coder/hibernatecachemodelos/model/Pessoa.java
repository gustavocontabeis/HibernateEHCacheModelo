package br.com.coder.hibernatecachemodelos.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "pessoa")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="pessoa")
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pessoa")
	private long id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "salario")
	private Double salario;

	@OneToOne(mappedBy = "pessoa")
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	private Endereco endereco;
	
	public Pessoa() {
		super();
	}

	public Pessoa(long id, String nome, Double salario, Endereco endereco) {
		super();
		this.id = id;
		this.nome = nome;
		this.salario = salario;
		this.endereco = endereco;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Override
	public String toString() {
		return "Pessoa ["
				+ "id=" + id 
				+ ", nome=" + nome 
				+ ", salario=" + salario 
				+ ", endereco=" + endereco 
				+ "]";
	}
	
}
