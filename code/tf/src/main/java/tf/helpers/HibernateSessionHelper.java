package tf.helpers;
 
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
 
/**
 * Classe que simplifica o processo de obtenção de sessões no Hibernate,
 * garantindo que cada thread trabalhe com a sua session.
 * @author chester
 *
 */
public class HibernateSessionHelper {
    private static final SessionFactory sessionFactory;
    
    static {
        try {            
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable e) {
            System.err.println("Erro criando objeto SessionFactory." 
                + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    /**
     * Retorna uma session gerada pela SessionFactory associada a esta thread
     * @return Session pronta para uso
     */
    public static Session getSession() {
    	return getSessionFactory().getCurrentSession();
    }
}
 
