package tf.codigodinamico;

import java.util.Map;

/**
 * Classe-base para os códigos gerados dinamicamente para cada passo.
 * <p>
 * Ela existe apenas para simplificar a chamada - o código dinâmico irá
 * implementar o método <code>executa</code> inserindo as atribuições dos dados
 * de entrada, o código-fonte digitado e o código para retornar a saída como um
 * mapa de objetos serializados (cuja chave é o nome do parâmetro de saída)
 * 
 * @author chester
 * 
 */
public abstract class Base {

	public abstract Map<String, Object> executa(Map<String, Object> __entrada);

}
