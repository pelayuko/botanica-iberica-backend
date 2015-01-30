package org.pelayo.storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(locations = "classpath:fotos.properties", ignoreUnknownFields = false, prefix = "flora")
public class FotoFloraConfiguration {

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
