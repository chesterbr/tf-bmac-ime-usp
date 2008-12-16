package tf.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;

import org.hibernate.Session;
import org.hibernate.Transaction;

import tf.helpers.HibernateSessionHelper;
import tf.helpers.MatrixHelper;
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
	private List<String> valoresEntrada;
	private List<String> valoresSaida;

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
		Transaction t = s.beginTransaction();
		this.aulas = (List<Aula>) s.createQuery("from Aula order by titulo")
				.list();
		t.commit();
		if (this.getUsuario().isProfessor())
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

	/**
	 * Abre o passo atual para experimentação
	 * 
	 * @return
	 */
	public Resolution abrirPasso() {
		recuperaPasso();
		this.aula = this.passo.getAula();
		return new ForwardResolution("/aluno/passo.jsp");
	}

	/**
	 * Recupera passo atual do banco (quando só vem o ID)
	 */
	protected void recuperaPasso() {
		Session s = HibernateSessionHelper.getSession();
		Transaction t = s.beginTransaction();
		this.setPasso((Passo) s.get(Passo.class, this.getPasso().getId()));
		t.commit();
	}

	/**
	 * Roda o código correspondente ao passo, encaminhando o usuário para a tela
	 * com os dados de retorno
	 * 
	 * @return
	 */
	public Resolution executarPasso() {
		// Monta o mapa com os dados de entrada, convertendo as strings para
		// objetos apropriadamente
		this.recuperaPasso();
		Map<String, Object> entrada = new HashMap<String, Object>();
		for (Parametro p : this.passo.getParametrosEntrada()) {
			String valor = this.valoresEntrada.get(p.getOrdem());
			if (p.getClasse().equals("java.lang.Integer")) {
				entrada.put(p.getNome(), new Integer(valor));
			} else if (p.getClasse().equals("java.lang.Double")) {
				entrada.put(p.getNome(), new Double(valor));
			} else if (p.getClasse().equals("Jama.Matrix")) {
				entrada.put(p.getNome(), MatrixHelper.stringToMatrix(valor));
			} else {
				entrada.put(p.getNome(), valor);
			}
		}
		// Chama o código, retornando eventuais erros à página original
		Map<String, Object> saida;
		try {
			saida = this.passo.executa(entrada);
		} catch (Exception e) {
			System.err.println("Erro ao rodar passo:");
			e.printStackTrace();
			this.getContext().getValidationErrors().addGlobalError(
					new SimpleError(e.getMessage()));
			return new ForwardResolution("/aluno/passo.jsp");
		}
		// Monta o array de saída (os índices correspondem à ordem do parâmetro
		// e os elementos são os valores, como no de entrada)
		valoresSaida = new ArrayList<String>();
		for (Parametro p : this.passo.getParametrosSaida())
			while (valoresSaida.size() < (p.getOrdem() + 1))
				valoresSaida.add(null);
		for (Parametro p : this.passo.getParametrosSaida()) {
			Object valor = saida.get(p.getNome());
			String strValor;
			if (valor instanceof Jama.Matrix) {
				StringWriter sw = new StringWriter();
				((Jama.Matrix)valor).print(new PrintWriter(sw), 8, 4);
				strValor = sw.toString();
			} else {
				strValor = valor.toString();
			}
			valoresSaida.set(p.getOrdem(), strValor);
		}
		return new ForwardResolution("/aluno/passo.jsp");
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		if (this.usuario == null)
			this.usuario = (Usuario) context.getRequest().getSession()
					.getAttribute("usuario");
		return this.usuario;
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
		return "{\"java.lang.Integer\":\"Inteiro\",\"java.lang.Double\":\"Real\",\"Jama.Matrix\":\"Matriz\"}";
	}

	public void setValoresEntrada(List<String> valoresEntrada) {
		this.valoresEntrada = valoresEntrada;
	}

	public List<String> getValoresEntrada() {
		return valoresEntrada;
	}

	public void setValoresSaida(List<String> valoresSaida) {
		this.valoresSaida = valoresSaida;
	}

	public List<String> getValoresSaida() {
		return valoresSaida;
	}

}
