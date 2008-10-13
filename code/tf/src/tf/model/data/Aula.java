package tf.model.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToOne;

@Entity
public class Aula {

	
	@Id;
	public long id;
	
	public String titulo;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	private List<Passo> passos = new ArrayList<Passo>();

	@OneToMany()
	public List<Passo> getPassos() {
		return passos;
	};

}
