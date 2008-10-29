package tf.model.data;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Parametro {

	private long id;
	private String nome;
	private int ordem;
	private String classe;
	private boolean tipoEntrada;
	
	
	@Id
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
	
	
}
