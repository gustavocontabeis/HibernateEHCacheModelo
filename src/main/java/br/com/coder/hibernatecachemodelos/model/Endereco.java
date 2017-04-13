package br.com.coder.hibernatecachemodelos.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "endereco")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="pessoa")
public class Endereco {

	@Id
	@Column(name = "id_endereco", unique = true, nullable = false)
	@GeneratedValue(generator = "gen")
	@GenericGenerator(name = "gen", strategy = "foreign", parameters = { @Parameter(name = "property", value = "pessoa") })
	private Long id;

	@Column(name = "rua")
	private String rua;

	@Column(name = "cep")
	private String cep;

	@Column(name = "cidade")
	private String cidade;

	@OneToOne
	@PrimaryKeyJoinColumn
	private Pessoa pessoa;
	
	public Endereco() {
		super();
	}

	public Endereco(Long id, String rua, String cep, String cidade, Pessoa pessoa) {
		super();
		this.id = id;
		this.rua = rua;
		this.cep = cep;
		this.cidade = cidade;
		this.pessoa = pessoa;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@Override
	public String toString() {
		return "Endereco ["
				+ "id=" + id 
				+ ", rua=" + rua 
				+ ", cep=" + cep 
				+ ", cidade=" + cidade 
				//+ ", pessoa=" + pessoa
				+ "]";
	}
	
	
	
}
