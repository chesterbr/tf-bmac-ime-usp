package tf.model.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Valor {
	
	private long id;
	private Entrada entrada;
	private Parametro parametro;
	private String valor;

	@Id
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	
	@ManyToOne
	@JoinColumn(name="entrada_id")	
	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	@ManyToOne
	@JoinColumn(name="parametro_id")	
	public Parametro getParametro() {
		return parametro;
	}

	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
