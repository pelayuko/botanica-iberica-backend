package org.pelayo;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.pelayo.filter.encoding.MyCharacterEncodingFilter;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

public class ServletInitializer extends SpringBootServletInitializer {

	@Bean
	public FilterRegistrationBean userEncodingRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		MyCharacterEncodingFilter userFilter = new MyCharacterEncodingFilter();
		registrationBean.setFilter(userFilter);
		registrationBean.setOrder(Integer.MAX_VALUE);
		return registrationBean;
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BotanicaBackendApplication.class);
	}

}
