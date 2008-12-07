package tf.model.data;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import net.sf.json.JSONObject;

@Entity
public class Parametro {

	private long id;
	private String nome;
	private String descricao;
	private int ordem;
	private String classe;
	private boolean tipoEntrada;
	private Passo passo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getClasse() {
		return classe;
	}

	public void setTipoEntrada(boolean entrada) {
		this.tipoEntrada = entrada;
	}

	public boolean isTipoEntrada() {
		return tipoEntrada;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setPasso(Passo passo) {
		this.passo = passo;
	}

	@ManyToOne
	public Passo getPasso() {
		return passo;
	}

}
