package tf.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import tf.model.data.Usuario;

/**
 * Garante que as páginas restritas vão passar primeiro pela autenticação, e que
 * alunos não terão acesso a áreas específicas de professores
 * 
 * TODO: se as áreas específicas forem fechadas nas actions, remover o
 * comentário acima
 * 
 * @author chester
 * 
 */
public class AcessoRestritoFilter implements javax.servlet.Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		Usuario u = (Usuario) req.getSession().getAttribute("usuario");
		String path = req.getServletPath();
		boolean isAutenticado = (u != null);
		boolean isProfessor = (isAutenticado && u.isProfessor());
		boolean libera = true;

		// Bloqueia páginas exclusivas para professores se não for um
		if ((path.equals("/AulasProfessor.action") || path.equals("/outro_path"))
				&& !isProfessor)
			libera = false;

		// Bloqueia qualquer página (exceto as abertas) se não for autenticado
		if ((!isAutenticado)
				&& !(req.getServletPath().equals("/Autenticacao.action")
						|| req.getServletPath().equals("/comum/login.jsp")
						|| req.getServletPath().equals("/comum/popula.jsp")
						|| req.getServletPath().startsWith("/stylesheets") || req
						.getServletPath().startsWith("/img")))
			libera = false;

		// Manda o usuário para a página, ou devolve para a tela de login
		if (libera)
			chain.doFilter(request, response);
		else
			req.getRequestDispatcher("/comum/login.jsp").forward(request,
					response);

	}

	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub

	}

}
