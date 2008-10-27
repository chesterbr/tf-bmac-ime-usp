package tf.model.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Passo {

	@Id
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	private long id;
	private String nome;
	private int ordem;
	private String explicacao_html;
	private String codigo_java;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public String getExplicacao_html() {
		return explicacao_html;
	}

	public void setExplicacao_html(String explicacao_html) {
		this.explicacao_html = explicacao_html;
	}

	public String getCodigo_java() {
		return codigo_java;
	}

	public void setCodigo_java(String codigo_java) {
		this.codigo_java = codigo_java;
	}


	
}
