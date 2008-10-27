package tf.model.data;

/**
 * Conjunto de dados de entrada para um um algoritmo.
 * <p>
 * O conjunto pertence a um <a href="Usuario">Usuario</a>, e se refere a um determinado passo de uma
 * aula. Ele é essencialmente uma composição de <a href="Valor">Valor</a>es.
 * 
 * @author chester
 */
public class Entrada {

	private String nome;
	private Usuario usuario;
	private Passo passo;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

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
