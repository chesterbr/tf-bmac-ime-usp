package tf.model.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Conjunto de dados de entrada para um um algoritmo.
 * <p>
 * O conjunto pertence a um <a href="Usuario">Usuario</a>, e se refere a um
 * determinado passo de uma aula. Ele é essencialmente uma composição de <a
 * href="Valor">Valor</a>es.
 * 
 * @author chester
 */
@Entity
public class Entrada {

	private long id;
	private String nome;
	private Usuario usuario;
	private Passo passo;

	@Id
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}	
	
	@ManyToOne
	@JoinColumn(name="usuario_id")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne
	@JoinColumn(name="passo_id")
	public Passo getPasso() {
		return passo;
	}

	public void setPasso(Passo passo) {
		this.passo = passo;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
