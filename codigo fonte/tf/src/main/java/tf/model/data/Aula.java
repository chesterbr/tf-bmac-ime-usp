package tf.model.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;



@Entity
public class Aula {
	private Long id;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String titulo;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	private List<Passo> passos = new ArrayList<Passo>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="aula", cascade=CascadeType.ALL)
	@JoinColumn(name="aula_id")
	@OrderBy("ordem")
	public List<Passo> getPassos() {
		return passos;
	}

	public void setPassos(List<Passo> passos) {
		this.passos = passos;
	};


	

}
