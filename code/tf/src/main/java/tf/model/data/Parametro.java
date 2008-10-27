package tf.model.data;

import javax.persistence.Id;

import org.hibernate.annotations.Entity;

@Entity
public class Parametro {

	private long id;
	private String nome;
	private int ordem;
	private String classe;
	private boolean entrada;
	
	
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

	public void setEntrada(boolean entrada) {
		this.entrada = entrada;
	}

	public boolean isEntrada() {
		return entrada;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public int getOrdem() {
		return ordem;
	}
	
	
}
