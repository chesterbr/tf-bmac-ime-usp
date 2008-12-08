package tf.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.hibernate.Session;
import org.hibernate.Transaction;

import tf.helpers.HibernateSessionHelper;
import tf.model.data.Aula;
import tf.model.data.Parametro;
import tf.model.data.Passo;
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
	protected Passo passo;

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

	/**
	 * Inicia a execução de uma aula, abrindo seu primeiro passo
	 * 
	 * @return
	 */
	public Resolution abrir() {
		if (this.aula == null)
			return new ForwardResolution(AulasProfessorActionBean.class,
					"lista");
		Session s = HibernateSessionHelper.getSession();
		Transaction t = s.beginTransaction();
		this.aula = (Aula) s.get(Aula.class, this.aula.getId());
		this.passo = this.aula.getPassos().get(0);
		t.commit();
		return new ForwardResolution("/aluno/passo.jsp");
	}

	public Resolution executarPasso() {
		System.out.println("CHESTER:ENTROU");
		Map<String, Object> entrada = new HashMap<String,Object>();
		for(Parametro p:this.passo.getParametrosEntrada()) {
			String valor = (String)this.getContext().getRequest().getAttribute(p.getNome());
			System.out.println("CHESTER:"+p.getNome()+"="+valor);
		}
		//this.passo.executa(entrada);
		return new ForwardResolution("/aluno/passo.jsp");		
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

	public void setPasso(Passo passo) {
		this.passo = passo;
	}

	public Passo getPasso() {
		return passo;
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
