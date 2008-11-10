package tf.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.sun.tools.javac.util.Context;

/**
 * Garante que as páginas restritas vão passar primeiro pela autenticação
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
		// Se houver usuário autenticado na sessão (ou se for a própria página
		// de login
		// ou seu back-end), beleza, senão exige autenticação
		HttpServletRequest req = (HttpServletRequest) request;
		if (req.getServletPath().equals("/Autenticacao.action")
				|| req.getServletPath().equals("/comum/login.jsp")
				|| req.getServletPath().equals("/comum/popula.jsp")
				|| req.getServletPath().startsWith("/stylesheets")
				|| req.getServletPath().startsWith("/img")
				|| (req.getSession().getAttribute("usuario") != null)) {
			chain.doFilter(request, response);
		} else {
			req.getRequestDispatcher("/comum/login.jsp").forward(request, response);
		}

	}

	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub

	}

}
