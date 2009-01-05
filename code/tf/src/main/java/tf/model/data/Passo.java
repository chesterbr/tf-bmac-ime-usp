package tf.model.data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import tf.codigodinamico.Base;
import tf.helpers.ConfigHelper;
import tf.helpers.HibernateSessionHelper;

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
	private String codigo_java_auxiliar;
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

	@Transient
	public String getExplicacao_html_formatada() {
		return getExplicacao_html().replaceAll("(\r\n|\r|\n|\n\r)", "<br/>");

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

	public void setCodigo_java_auxiliar(String codigo_java_auxiliar) {
		this.codigo_java_auxiliar = codigo_java_auxiliar;
	}

	public String getCodigo_java_auxiliar() {
		return codigo_java_auxiliar;
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

	/**
	 * Recupera apenas os parâmetros de entrada ou saída
	 * 
	 * @param isEntrada
	 *            true se for para recuperar os de entrada, false se for os de
	 *            saída
	 * @return parâmetros do passo, filtrados de acordo com
	 *         <code>isEntrada</code>
	 */
	@Transient
	public List<Parametro> getParametrosPorTipo(boolean isEntrada) {
		List<Parametro> filtrados = new ArrayList<Parametro>();
		for (Parametro p : this.getParametros())
			if (p.isTipoEntrada() == isEntrada)
				filtrados.add(p);
		return filtrados;
	}

	/**
	 * Recupera apenas os parâmetros de entrada
	 * 
	 * @return
	 */
	@Transient
	public List<Parametro> getParametrosEntrada() {
		return getParametrosPorTipo(true);
	}

	/**
	 * Recupera apenas os parâmetros de saída
	 * 
	 * @return
	 */
	@Transient
	public List<Parametro> getParametrosSaida() {
		return getParametrosPorTipo(false);
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
	 * @return Nome da classe correspondente ao código deste passo
	 */
	@Transient
	private String getNomeClasse() {
		return "Passo" + getId();
	}

	/**
	 * @return Nome do pacote correspondente ao código deste passo
	 */
	@Transient
	private String getNomePackage() {
		return "tf.codigodinamico";
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
		// sequencia se for 0
		StringBuilder fonte = new StringBuilder();
		fonte.append("package " + this.getNomePackage() + ";\n");
		fonte.append("import java.util.*;\n");
		fonte.append("import Jama.*;\n");
		fonte.append("public class " + this.getNomeClasse()
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
		fonte.append(";;\n"); // Importante para que os erros não "vazem" para a
		// parte de baixo
		// Monta o mapa com os parâmetros de saída, com statements no formato:
		// __saida.add("nome",nome);
		fonte
				.append("  Map<String,Object> __saida = new HashMap<String,Object>();\n");
		if (this.getParametros() != null)
			for (Parametro p : this.getParametros())
				// if (!p.isTipoEntrada())
				fonte.append("  __saida.put(\"").append(p.getNome()).append(
						"\",").append(p.getNome()).append(");\n");
		fonte.append("  return __saida;\n");
		fonte.append(" }\n");
		
		// Enxerta o código auxiliar
		fonte.append('\n').append(this.getCodigo_java_auxiliar()).append('\n');
		fonte.append(";;\n"); // Importante para que os erros não "vazem" para a
		// parte de baixo

		fonte.append("}\n");
		System.out.println(fonte);

		// Prepara o compilador
		javax.tools.JavaCompiler compilador = ToolProvider
				.getSystemJavaCompiler();
		JavaSourceFromString javaString = new JavaSourceFromString(this
				.getNomePackage()
				+ "." + this.getNomeClasse(), fonte.toString());
		ArrayList<JavaSourceFromString> al = new ArrayList<JavaSourceFromString>();
		al.add(javaString);

		// Saída (erros, etc.)
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(baos);

		// Diretório-destino e classpath
		// TODO detectar o diretório correto (carregando a classe-base?)
		List<String> opcoes = new ArrayList<String>();
		opcoes.add("-d");
		opcoes.add(ConfigHelper.getClasspathDinamico());
		opcoes.add("-cp");
		opcoes.add(ConfigHelper.getClaspathApp());
		// "myAppPath/WEB_INF/lib/test.jar;myAppPath/WEB_INF/lib/test2.jar");

		// Compila
		JavaCompiler.CompilationTask task = compilador.getTask(osw, null, null,
				opcoes, null, al);
		boolean sucesso = task.call();

		this.errosDeCompilacao = baos.toString();
		return sucesso;
	}

	public Set<Valor> executa(Entrada entrada) {
		throw new RuntimeException("Não implementado!");
	}

	/**
	 * Executa o código deste passo para uma entrada em memória
	 * 
	 * @param entrada
	 *            Dados de entrada (chave=nome, objeto=valor)
	 * @return Dados de entrada (que podem ter sido alterados) e de saída (chave=nome, objeto=valor)
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> executa(Map<String, Object> entrada)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		// TODO só fazer isso se não tiver sido feito ainda
		if (!this.compila()) {
			throw new ClassNotFoundException("Erro na compilação:"
					+ this.getErrosDeCompilacao());
		}
		// Carrega a classe
		URL[] urls = new URL[1];
		try {
			urls[0] = new File(ConfigHelper.getClasspathDinamico()).toURI()
					.toURL();
		} catch (MalformedURLException e) {
			System.err.println("CHESTER:ERROURL");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ClassLoader cl = new URLClassLoader(urls, Thread.currentThread()
				.getContextClassLoader());
		Class<Base> classe;
		Base codigo;
		classe = (Class<Base>) cl.loadClass(this.getNomePackage() + "."
				+ this.getNomeClasse());
		codigo = (Base) classe.getConstructor(new Class[0]).newInstance(
				new Object[0]);

		// Executa o método
		Map<String, Object> resultado = codigo.executa(entrada);

		// Descarrega a classe (matando as referências ao objeto, a ela e ao
		// classloader)
		ReferenceQueue weakQueueCl = new ReferenceQueue();
		WeakReference weakRefCl = new WeakReference(classe, weakQueueCl);
		weakRefCl.enqueue();

		codigo = null;
		classe = null;
		cl = null;
		System.gc();
		System.gc();
		return resultado;
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

	/**
	 * @return passo seguinte dentro da aula, ou <code>null</code> se não houver
	 */
	@Transient
	public Passo getProximo() {
		return getVizinho(true);
	}

	/**
	 * @return passo anterior dentro da aula, ou <code>null</code> se não houver
	 */
	@Transient
	public Passo getAnterior() {
		return getVizinho(false);
	}

	@Transient
	private Passo getVizinho(boolean proximo) {
		if (this.getAula() != null) {
			List<Passo> passos = this.getAula().getPassos();
			int posOutroPasso = passos.indexOf(this) + (proximo ? 1 : -1);
			if (posOutroPasso >= 0 && posOutroPasso < passos.size())
				return passos.get(posOutroPasso);
		}
		return null;
	}

}
