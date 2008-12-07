package tf.model.data;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.tools.JavaCompiler;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Passo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private long id;
	private String nome;
	private int ordem;
	private String explicacao_html;
	private String codigo_java;
	private Aula aula;
	private List<Parametro> parametros = new ArrayList<Parametro>();
	private String errosDeCompilacao;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public String getExplicacao_html() {
		return explicacao_html;
	}

	public void setExplicacao_html(String explicacao_html) {
		this.explicacao_html = explicacao_html;
	}

	public String getCodigo_java() {
		return codigo_java;
	}

	public void setCodigo_java(String codigo_java) {
		this.codigo_java = codigo_java;
	}

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	@ManyToOne
	public Aula getAula() {
		return aula;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	@Transient
	public String getErrosDeCompilacao() {
		return errosDeCompilacao;
	}

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "passo", cascade = CascadeType.ALL)
	@JoinColumn(name = "passo_id")
	@OrderBy("ordem")
	public List<Parametro> getParametros() {
		return parametros;
	}

	@Transient
	public String getParametrosAsJSON() {

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			public boolean apply(Object source, String name, Object value) {
				return (value != null && name.equals("passo"));
			}
		});

		return JSONArray.fromObject(getParametros(), jsonConfig).toString();
	}

	@SuppressWarnings("unchecked")
	public void setParametrosAsJSON(String json) {
		List<Parametro> params = (List<Parametro>) JSONArray.toCollection(
				JSONArray.fromObject(json), Parametro.class);
		for (Parametro parametro : params) {
			parametro.setPasso(this);
		}
		this.setParametros(params);
	}

	/**
	 * Compila o código correpondente ao objeto.
	 * <p>
	 * O nome da classe depende do ID, portanto é preciso estudar um esquema de
	 * thread safety para os novos registros.
	 * 
	 * @return
	 */
	public boolean compila() {

		// Monta o código-fonte
		String nomeClasse = "Passo" + getId(); // TODO gerar pelo objeto, c/
		// sequencia se for 0
		StringBuilder fonte = new StringBuilder();
		fonte.append("package tf.codigodinamico;\n");
		fonte.append("import java.util.*;\n");
		fonte.append("public class " + nomeClasse
				+ " extends tf.codigodinamico.Base {\n");
		fonte
				.append(" public Map<String, Object> executa(Map<String, Object> __entrada) {\n");
		// Recupera os parâmetros de entrada do mapa, com statements no formato:
		// Classe nome = (Classe)__entrada.get("nome");
		// e declara os de saída como simplesmente
		// Classe nome = null;
		if (this.getParametros() != null)
			for (Parametro p : this.getParametros()) {
				fonte.append("  ").append(p.getClasse()).append(' ').append(
						p.getNome()).append(" = ");
				if (p.isTipoEntrada())
					fonte.append('(').append(p.getClasse()).append(
							")__entrada.get(\"").append(p.getNome()).append(
							"\")");
				else
					fonte.append("null");
				fonte.append(";\n");
			}
		// Enxerta o código escrito pelo professor
		fonte.append('\n').append(this.getCodigo_java()).append('\n');
		fonte.append(";\n"); // Importante para que os erros não "vazem" para a parte de baixo
		// Monta o mapa com os parâmetros de saída, com statements no formato:
		// __saida.add("nome",nome);
		fonte
				.append("  Map<String,Object> __saida = new HashMap<String,Object>();\n");
		if (this.getParametros() != null)
			for (Parametro p : this.getParametros())
				if (!p.isTipoEntrada())
					fonte.append("  __saida.put(\"").append(p.getNome())
							.append("\",").append(p.getNome()).append(");\n");
		fonte.append("  return __saida;\n");
		fonte.append(" }\n");
		fonte.append("}\n");
		System.out.println(fonte);

		// Prepara o compilador
		javax.tools.JavaCompiler compilador = ToolProvider
				.getSystemJavaCompiler();
		JavaSourceFromString javaString = new JavaSourceFromString(
				"tf.codigodinamico." + nomeClasse, fonte.toString());
		ArrayList<JavaSourceFromString> al = new ArrayList<JavaSourceFromString>();
		al.add(javaString);

		// Saída (erros, etc.)
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(baos);

		// Diretório-destino e classpath
		// TODO detectar o diretório correto (carregando a classe-base?)
		List<String> opcoes = new ArrayList<String>();
		opcoes.add("-d");
		opcoes.add("target/classes/");
		 opcoes.add("-cp");
		 opcoes.add("/Users/chester/Documents/workspace/tf/target/classes");
		// "myAppPath/WEB_INF/lib/test.jar;myAppPath/WEB_INF/lib/test2.jar");

		// Compila
		JavaCompiler.CompilationTask task = compilador.getTask(osw, null, null,
				opcoes, null, al);
		boolean sucesso = task.call();

		this.errosDeCompilacao = baos.toString();
		return sucesso;
	}

	public Set<Valor> executa(Entrada entrada) {

		return null;
	}

	public static void main(String args[]) {
		Passo p = new Passo();
		p.setCodigo_java("dard;\n");
		List<Parametro> l1 = new ArrayList<Parametro>();
		Parametro p1 = new Parametro();
		p1.setNome("aaa");
		p1.setClasse("java.lang.Integer");
		p1.setTipoEntrada(true);
		l1.add(p1);
		Parametro p2 = new Parametro();
		p2.setNome("bbb");
		p2.setClasse("java.lang.Double");
		p2.setTipoEntrada(false);
		l1.add(p2);
		p.setParametros(l1);

		p.compila();
	}

	/**
	 * A file object used to represent source coming from a string. This example
	 * is from the JavaDocs for JavaCompiler.
	 */
	class JavaSourceFromString extends SimpleJavaFileObject {
		/**
		 * The source code of this "file".
		 */
		final String code;

		/**
		 * Constructs a new JavaSourceFromString.
		 * 
		 * @param name
		 *            the name of the compilation unit represented by this file
		 *            object
		 * @param code
		 *            the source code for the compilation unit represented by
		 *            this file object
		 */
		JavaSourceFromString(String name, String code) {
			super(URI.create("string:///" + name.replace('.', '/')
					+ Kind.SOURCE.extension), Kind.SOURCE);
			this.code = code;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return code;
		}
	}

}
