package org.pelayo;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.FileNotFoundException;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class BotanicaBackendApplication {

	@Value("${general.Access-Control-Allow-Origin}")
	private String allowOrigin;

	public static void main(String[] args) {
		SpringApplication.run(BotanicaBackendApplication.class, args);
	}

	@Bean
	public TomcatConnectorCustomizer containerCustomizer()  throws FileNotFoundException {

		return new TomcatConnectorCustomizer() {
			@Override
			public void customize(Connector connector) {
				connector.setURIEncoding("UTF-8");
			}
		};
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins(allowOrigin);
			}
		};
	}
}
