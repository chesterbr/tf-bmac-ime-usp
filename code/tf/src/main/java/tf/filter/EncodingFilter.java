package tf.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Ajusta problemas de acentuação (encoding).
 * <p>
 * Vide: http://cagan327.blogspot.com/2006/05/utf-8-encoding-fix-tomcat-jsp-etc.html
 * @author Cagan Senturk
 *
 */
public class EncodingFilter implements Filter {
	private String encoding;
	private FilterConfig filterConfig;

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig fc) throws ServletException {
		this.filterConfig = fc;
		this.encoding = filterConfig.getInitParameter("encoding");
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		req.setCharacterEncoding(encoding);
		chain.doFilter(req, resp);
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

}