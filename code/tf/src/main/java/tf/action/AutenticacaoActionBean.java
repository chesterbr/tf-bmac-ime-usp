package tf.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.hibernate.Session;

import tf.helpers.HibernateSessionHelper;
import tf.model.data.Usuario;

/**
 * Actions relacionadas a autenticação (login, criação de usuário, etc.)
 * 
 * @author chester
 */
public class AutenticacaoActionBean implements ActionBean {

	@Validate(required = true)
	private String email;
	@Validate(required = true)
	private String senha;

	private Usuario usuario;

	private ActionBeanContext context;

	public ActionBeanContext getContext() {
		return context;
	}

	public void setContext(ActionBeanContext context) {
		this.context = context;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenha() {
		return senha;
	}

	/**
	 * Esta validação verifica se o email/senha correspondem a um usuário (e já
	 * guarda ele no bean)
	 * 
	 * @param errors
	 */
	@ValidationMethod(on = "login")
	public void validaLogin(ValidationErrors errors) {
		Session session = HibernateSessionHelper.getSession();
		session.beginTransaction();
		this.usuario = (Usuario) session.createQuery(
				"from Usuario where email=?").setString(0, email)
				.uniqueResult();
		if (usuario == null || !usuario.isSenhaValida(senha)) {
			errors.add("email", new SimpleError(
					"Usu&aacute;rio/senha inv&aacute;lidos"));
		}
		session.close();
	}

	@DefaultHandler
	public Resolution login() {
		if (usuario.isProfessor())
			return new ForwardResolution("/professor/aulas.jsp");
		else
			return new ForwardResolution("/aluno/aulas.jsp");
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

}
