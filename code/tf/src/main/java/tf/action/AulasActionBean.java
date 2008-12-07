package tf.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import net.sf.json.JSONObject;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.hibernate.Session;
import org.hibernate.Transaction;

import tf.helpers.HibernateSessionHelper;
import tf.model.data.Aula;
import tf.model.data.Usuario;

/**
 * Eventos relacionados a aula (listar, executar, etc.) disponíveis para
 * professores e alunos
 * 
 * @author chester
 */
public class AulasActionBean implements ActionBean {

	private Usuario usuario;
	private List<Aula> aulas;
	protected Aula aula;

	private ActionBeanContext context;

	public ActionBeanContext getContext() {
		return context;
	}

	public void setContext(ActionBeanContext context) {
		this.context = context;
	}

	/**
	 * Mostra a lista de aulas disponível para o usuário autenticado
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@DefaultHandler
	public Resolution lista() {
		Session s = HibernateSessionHelper.getSession();
		this.usuario = (Usuario) context.getRequest().getSession()
				.getAttribute("usuario");
		Transaction t = s.beginTransaction();
		this.aulas = (List<Aula>) s.createQuery("from Aula order by titulo")
				.list();
		t.commit();
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

	public void setAulas(List<Aula> aulas) {
		this.aulas = aulas;
	}

	public List<Aula> getAulas() {
		return aulas;
	}

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	public Aula getAula() {
		return aula;
	}

	/**
	 * Monta uma lista (fixa) de classes de dados possíveis para parâmetros de
	 * entrada/saída dos passos de uma aula.
	 * <p>
	 * No futuro esta lista pode ter sua própria tabela/entidade, e esse método
	 * passaria a ser apenas um helper
	 * 
	 * @return objeto JSON cujos campos são os tipos de dados (ex.:
	 *         java.lang.Integer) e cujos valores as descrições dos tipos (ex.:
	 *         "Número Inteiro").
	 */
	public String getClassesAsJSON() {
		return "{\"java.lang.Integer\":\"Inteiro\",\"java.lang.Double\":\"Real\"}";
	}

}
