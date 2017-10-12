package org.pelayo.filter.encoding;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;

@Component
public class MyCharacterEncodingFilter extends CharacterEncodingFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		// request.setCharacterEncoding("ISO-8859-1");
		response.setCharacterEncoding("UTF-8");

		// filterChain.doFilter(request, response);
		// FIXME!!: ugly workaround...
		filterChain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
	}

}
