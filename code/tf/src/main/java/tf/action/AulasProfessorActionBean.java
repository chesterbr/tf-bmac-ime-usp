package tf.action;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;

import org.hibernate.Session;
import org.hibernate.Transaction;

import tf.helpers.HibernateSessionHelper;
import tf.model.data.Aula;

/**
 * Eventos sobre aulas de acesso exclusivo do professor
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
		if ((this.aula == null) || (this.aula.titulo==null)) {
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
			return new ForwardResolution(AulasProfessorActionBean.class, "lista");
		Session s = HibernateSessionHelper.getSession();
		Transaction t = s.beginTransaction();
		this.aula = (Aula) s.get(Aula.class, this.aula.getId());
		t.commit();
		return new ForwardResolution("/professor/aula.jsp");
	}

	public Resolution apagar() {
		if (this.aula == null)
			return new ForwardResolution(AulasProfessorActionBean.class, "lista");
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

}
