package tf.model.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import tf.helpers.EncryptionHelper;

/**
 * Representa uma pessoa que interage com o sistema, contendo suas credenciais e
 * seu tipo (professor ou aluno)
 * 
 * @author chester
 * 
 */
@Entity
public class Usuario {

	private Long id;
	private String email;
	private String hash_senha;
	private String nome;
	private boolean professor;

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHash_senha() {
		return hash_senha;
	}

	public void setHash_senha(String hash_senha) {
		this.hash_senha = hash_senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isProfessor() {
		return professor;
	}

	public void setProfessor(boolean professor) {
		this.professor = professor;
	}

	public boolean isSenhaValida(String senha) {
		return EncryptionHelper.getInstance().gera_hash_senha(senha).equals(
				this.hash_senha);
	}
	
	public void setSenha(String senha) {
		setHash_senha(EncryptionHelper.getInstance().gera_hash_senha(senha));
	}

}
