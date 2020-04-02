package curso.springboot.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Usuarios implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull(message = "nome não pode ser nulo")
	@NotEmpty(message = "nome não pode ser vazio")
	private String nome;
	
	@NotNull(message = "sobrenome não pode ser nulo")
	@NotEmpty(message = "sobrenome não pode ser vazio")
	private String sobrenome;
	@Min(value = 18,message = "Idade invalida")
	private int idade;
	
	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Telefones> telefones;
	@ManyToOne
	private Profissao prof;
	
	private String cep;
	
	private String rua;
	
	private String bairro;
	
	private String cidade;
	
	private String uf;
	
	private String ibge;
	
	private String sexo;
	@Lob
	private byte[] curriculo;
	
	private String nomeFileCurriculo;
	
	private String tipoFileCurriculo;
	
	public String getNomeFileCurriculo() {
		return nomeFileCurriculo;
	}

	public void setNomeFileCurriculo(String nomeFileCurriculo) {
		this.nomeFileCurriculo = nomeFileCurriculo;
	}

	public String getTipoFileCurriculo() {
		return tipoFileCurriculo;
	}

	public void setTipoFileCurriculo(String tipoFileCurriculo) {
		this.tipoFileCurriculo = tipoFileCurriculo;
	}
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@Enumerated(EnumType.STRING)
	private Cargo cargo;
	
	public void setCurriculo(byte[] curriculo) {
		this.curriculo = curriculo;
	}
	
	public byte[] getCurriculo() {
		return curriculo;
	}
	
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	public Cargo getCargo() {
		return cargo;
	}
	public void setProf(Profissao prof) {
		this.prof = prof;
	}
	public Profissao getProf() {
		return prof;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getIbge() {
		return ibge;
	}
	public void setIbge(String ibge) {
		this.ibge = ibge;
	}
	public void setTelefones(List<Telefones> telefones) {
		this.telefones = telefones;
	}
	public List<Telefones> getTelefones() {
		return telefones;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public int getIdade() {
		return idade;
	}
	public void setIdade(int idade) {
		this.idade = idade;
	}
}
