package tf.helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Recupera configrações guardadas no tf.properties
 * 
 * @author chester
 * 
 */
public class ConfigHelper {

	private static String getProperty(String nome) {
		Properties props = new Properties();
		try {
			props.load(ConfigHelper.class.getClassLoader().getResourceAsStream(
					"tf.properties"));
			String valor = props.getProperty(nome);
			return (valor == null ? "" : valor);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Erro ao ler tf.properties - verifique se foi copiado do tf.properties.exemplo e se os valores foram configurados corretamente",
					e);
		}
	}

	/**
	 * @return diretório com as classes da aplica��o
	 */
	public static String getClaspathApp() {
		return getProperty("classpath_app");
	}

	/**
	 * @return diretório no qual serão colocadas as classes compiladas
	 *         dinamicamente
	 */
	public static String getClasspathDinamico() {
		return getProperty("classpath_dinamico");
	}

}
