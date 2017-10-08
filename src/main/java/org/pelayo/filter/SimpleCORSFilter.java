package org.pelayo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimpleCORSFilter implements Filter {
	
	@Value("${general.Access-Control-Allow-Origin}")
	private String allowOrigin;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {

		HttpServletResponse test = (HttpServletResponse) res;
		test.setHeader("Access-Control-Allow-Origin", allowOrigin);
		test.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		test.setHeader("Access-Control-Max-Age", "3600");
		test.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		chain.doFilter(req, res);
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}

}
