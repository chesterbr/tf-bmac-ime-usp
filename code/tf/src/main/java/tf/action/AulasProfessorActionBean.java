package tf.action;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;

import org.hibernate.Session;
import org.hibernate.Transaction;

import tf.helpers.HibernateSessionHelper;
import tf.model.data.Aula;
import tf.model.data.Passo;

/**
 * Eventos do CRUD de aulas (e passos de aula) de acesso exclusivo do professor
 * 
 * @author chester
 */
public class AulasProfessorActionBean extends AulasActionBean {

	private Passo passo;

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
		this.setPasso(new Passo());
		this.getPasso().setAula(this.aula);
		return new ForwardResolution("/professor/passo.jsp");
	}

	public Resolution editarPasso() {
		if (this.getPasso() == null)
			return new ForwardResolution(AulasProfessorActionBean.class,
					"editar");
		Session s = HibernateSessionHelper.getSession();
		Transaction t = s.beginTransaction();
		this.setPasso((Passo) s.get(Passo.class, this.getPasso().getId()));
		t.commit();
		return new ForwardResolution("/professor/passo.jsp");
	}

	public Resolution salvarPasso() {
		Session s = HibernateSessionHelper.getSession();
		Transaction t = s.beginTransaction();
		this.passo.setAula((Aula) s.get(Aula.class, this.aula.getId()));
		s.merge(this.passo);
		t.commit();
		getContext().getMessages().add(
				new SimpleMessage("Passo \"" + this.getPasso().getNome()
						+ "\" salvo."));
		return new ForwardResolution(AulasProfessorActionBean.class, "editar");
	}

	public void setPasso(Passo passo) {
		this.passo = passo;
	}

	public Passo getPasso() {
		return passo;
	}

}
