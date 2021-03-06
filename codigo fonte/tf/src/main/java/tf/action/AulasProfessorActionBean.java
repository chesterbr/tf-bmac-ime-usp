package tf.action;

import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import tf.helpers.HibernateSessionHelper;
import tf.model.data.Aula;
import tf.model.data.Parametro;
import tf.model.data.Passo;

/**
 * Eventos do CRUD de aulas (e passos de aula) de acesso exclusivo do professor
 * 
 * @author chester
 */
public class AulasProfessorActionBean extends AulasActionBean {

	/**
	 * Permite ao professor entrar com os dados de uma nova aula
	 * 
	 * @return
	 */
	public Resolution nova() {
		this.aula = new Aula();
		return new ForwardResolution("/professor/aula.jsp");
	}

	public Resolution salvar() {
		if ((this.aula == null) || (this.aula.titulo == null)) {
			getContext().getValidationErrors().add("titulo",
					new SimpleError("Campos obrigatórios não-preenchidos"));
			return new ForwardResolution("/professor/aula.jsp");
		}
		System.out.println(this.getContext().getRequest()
				.getCharacterEncoding());
		System.out.println("CHESTER:DEBUG:" + this.aula.titulo);
		Session s = HibernateSessionHelper.getSession();
		Transaction t = s.beginTransaction();
		s.merge(this.aula);
		t.commit();
		getContext().getMessages().add(
				new SimpleMessage("Aula \"" + this.aula.titulo + "\" salva."));
		return new ForwardResolution(AulasProfessorActionBean.class, "lista");
	}

	public Resolution editar() {
		if (this.aula == null)
			return new ForwardResolution(AulasProfessorActionBean.class,
					"lista");
		Session s = HibernateSessionHelper.getSession();
		Transaction t = s.beginTransaction();
		this.aula = (Aula) s.get(Aula.class, this.aula.getId());
		t.commit();
		return new ForwardResolution("/professor/aula.jsp");
	}

	public Resolution apagar() {
		if (this.aula == null)
			return new ForwardResolution(AulasProfessorActionBean.class,
					"lista");
		Session s = HibernateSessionHelper.getSession();
		Transaction t = s.beginTransaction();
		this.aula = (Aula) s.get(Aula.class, this.aula.getId());
		String titulo = this.aula.titulo;
		s.delete(this.aula);
		t.commit();
		getContext().getMessages().add(
				new SimpleMessage("Aula \"" + titulo + "\" apagada."));
		return new ForwardResolution(AulasProfessorActionBean.class, "lista");
	}

	public Resolution novoPasso() {
		// Garante que ele fique sempre no fim da lista
		Session s = HibernateSessionHelper.getSession();
		Transaction t = s.beginTransaction();
		int ordem = (Integer) s.createQuery("select max(ordem) from Passo")
				.uniqueResult();
		t.commit();
		ordem++;
		// Cria o passo vazio e abre para editar
		Passo p = new Passo();
		p.setAula(this.aula);
		p.setOrdem(ordem);
		this.setPasso(p);
		return new ForwardResolution("/professor/passo.jsp");
	}

	public Resolution editarPasso() {
		if (this.getPasso() == null)
			return new ForwardResolution(AulasProfessorActionBean.class,
					"editar");
		this.recuperaPasso();
		return new ForwardResolution("/professor/passo.jsp");
	}

	public Resolution apagarPasso() {
		if (this.getPasso() != null) {
			this.recuperaPasso();
			Session s = HibernateSessionHelper.getSession();
			Transaction t = s.beginTransaction();
			s.delete(passo);
			t.commit();
		}
		return new ForwardResolution(AulasProfessorActionBean.class, "editar");
	}

	public Resolution moverPassoAcima() {
		return moverPasso(true);
	}

	public Resolution moverPassoAbaixo() {
		return moverPasso(false);
	}

	/**
	 * Move um passo acima ou abaixo (o código é quase o mesmo)
	 * 
	 * @param acima
	 * @return
	 */
	private Resolution moverPasso(boolean acima) {
		if (this.getPasso() != null) {
			this.recuperaPasso();
			List<Passo> passos = this.getPasso().getAula().getPassos();
			int posOutroPasso = passos.indexOf(passo) + (acima ? -1 : 1);
			if (posOutroPasso >= 0 && posOutroPasso < passos.size()) {
				Passo outroPasso = passos.get(posOutroPasso);
				int temp = passo.getOrdem();
				passo.setOrdem(outroPasso.getOrdem());
				outroPasso.setOrdem(temp);
				Session s = HibernateSessionHelper.getSession();
				Transaction t = s.beginTransaction();
				s.update(passo);
				s.update(outroPasso);
				t.commit();
			}
		}
		return new ForwardResolution(AulasProfessorActionBean.class, "editar");
	}

	public void setParam_nome(String s) {
		System.out.println("CHESTER:" + s);
	}

	public Resolution salvarPasso() {
		Session s = HibernateSessionHelper.getSession();
		Transaction t = s.beginTransaction();
		// Remove os parâmetros antigos, se houverem
		if (this.passo.getId() != 0)
			for (Parametro parametro : ((Passo) s.get(Passo.class, this.passo
					.getId())).getParametros())
				parametro.setPasso(null);
		// Guarda o passo (e, implicitamente, os novos/editados parâmetros)
		this.passo.setAula((Aula) s.get(Aula.class, this.aula.getId()));
		Passo novoPasso = (Passo) s.merge(this.passo);
		t.commit();
		getContext().getMessages().add(
				new SimpleMessage("Passo \"" + this.getPasso().getNome()
						+ "\" salvo."));
		this.setPasso(novoPasso);
		return new ForwardResolution(AulasProfessorActionBean.class, "editar");
	}

	/**
	 * Checa a sintaxe de um código, compilando-o
	 * 
	 * @param errors
	 */
	@ValidationMethod(on = "checarPasso")
	public void validaSintaxe(ValidationErrors errors) {
		this.passo.compila();
		String erros = this.passo.getErrosDeCompilacao();
		System.out.println("CHESTER: " + erros);
		if (erros != null & erros.length() > 0) {
			String msg = ("Os seguintes erros foram encontrados:\n" + StringEscapeUtils
					.escapeHtml(erros)).replace("\n", "<br/>").replace("{",
					"&#123;").replace("}", "&#125;");
			errors.add("passo.codigo_java", new SimpleError(msg));
		}
	}

	/**
	 * Salva os dados e exibe os erros do código (se houverem) - a checagem já
	 * foi feita na validação
	 * 
	 * @see #validaSintaxe(ValidationErrors)
	 * @return
	 */
	public Resolution checarPasso() {
		salvarPasso();
		return new ForwardResolution("/professor/passo.jsp");
	}

	public Resolution testarPasso() {
		salvarPasso();
		this.getContext().getMessages().clear();
		return abrirPasso();
	}

}
