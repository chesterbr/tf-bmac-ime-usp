package tf.helpers;
 
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
 
/**
 * Recupera configrações guardadas no tf.properties
 * @author chester
 *
 */
public class ConfigHelper {
    
	/**
	 * @return diretório com as classes da aplicação
	 */
	public static String getClaspathApp() {
		return "c:\\java\\tf\\code\\target\\classes";
	}
	
	/**
	 * @return diretório no qual serão colocadas as classes compiladas dinamicamente
	 */
	public static String getClasspathDinamico() {
		return "c:\\java\\classes_dinamicas";
	}
	
}
 
